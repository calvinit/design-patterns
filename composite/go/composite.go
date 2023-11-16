package composite

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"sync/atomic"
)

type FileSystemNode interface {
	fmt.Stringer
	GetPath() string
	CountNumOfFiles() int32
	CountSizeOfFiles() int64
}

// ============================================================

type File struct {
	Path string
}

func (f *File) GetPath() string {
	return f.Path
}

func (f *File) CountNumOfFiles() int32 {
	return 1
}

func (f *File) CountSizeOfFiles() int64 {
	fileInfo, _ := os.Stat(f.Path)
	return fileInfo.Size()
}

func (f *File) String() string {
	return fmt.Sprintf("File{path='%s', fileNums=%d, fileSizes=%d}", f.Path, f.CountNumOfFiles(), f.CountSizeOfFiles())
}

// ============================================================

type Directory struct {
	Path      string
	ParentDir *Directory
	SubNodes  SubNodes
	fileNums  int32
	fileSizes int64
}

func NewDirectory(path string, parentDir *Directory) *Directory {
	return &Directory{
		Path:      path,
		ParentDir: parentDir,
		SubNodes:  SubNodes{make([]FileSystemNode, 0)},
	}
}

func (d *Directory) GetPath() string {
	return d.Path
}

func (d *Directory) CountNumOfFiles() int32 {
	return d.fileNums
}

func (d *Directory) CountSizeOfFiles() int64 {
	return d.fileSizes
}

func (d *Directory) incrCachedCountNumOfFiles(delta int32) {
	atomic.AddInt32(&d.fileNums, delta)
	if d.ParentDir != nil {
		d.ParentDir.incrCachedCountNumOfFiles(delta)
	}
}

func (d *Directory) decrCachedCountNumOfFiles(delta int32) {
	atomic.AddInt32(&d.fileNums, -delta)
	if d.ParentDir != nil {
		d.ParentDir.decrCachedCountNumOfFiles(delta)
	}
}

func (d *Directory) incrCachedCountSizeOfFiles(delta int64) {
	atomic.AddInt64(&d.fileSizes, delta)
	if d.ParentDir != nil {
		d.ParentDir.incrCachedCountSizeOfFiles(delta)
	}
}

func (d *Directory) decrCachedCountSizeOfFiles(delta int64) {
	atomic.AddInt64(&d.fileSizes, -delta)
	if d.ParentDir != nil {
		d.ParentDir.decrCachedCountSizeOfFiles(delta)
	}
}

func (d *Directory) AddSubNode(fsn FileSystemNode) {
	if d.SubNodes.Add(fsn) {
		d.incrCachedCountNumOfFiles(fsn.CountNumOfFiles())
		d.incrCachedCountSizeOfFiles(fsn.CountSizeOfFiles())
	}
}

func (d *Directory) RemoveSubNode(fsn FileSystemNode) {
	if d.SubNodes.Remove(fsn) {
		d.decrCachedCountNumOfFiles(fsn.CountNumOfFiles())
		d.decrCachedCountSizeOfFiles(fsn.CountSizeOfFiles())
	}
}

func (d *Directory) String() string {
	return fmt.Sprintf("Directory{path='%s', fileNums=%d, fileSizes=%d, subs=%s}",
		d.Path, d.fileNums, d.fileSizes, d.SubNodes.String())
}

type SubNodes struct {
	nodes []FileSystemNode
}

func (sub *SubNodes) IndexOf(fsn FileSystemNode) int {
	idx := -1
	fsnPath := strings.ToLower(fsn.GetPath())
	for i, node := range sub.nodes {
		if strings.ToLower(node.GetPath()) == fsnPath {
			idx = i
			break
		}
	}
	return idx
}

func (sub *SubNodes) Add(fsn FileSystemNode) bool {
	if sub.IndexOf(fsn) == -1 {
		sub.nodes = append(sub.nodes, fsn)
		return true
	}
	return false
}

func (sub *SubNodes) Remove(fsn FileSystemNode) bool {
	if idx := sub.IndexOf(fsn); idx != -1 {
		sub.nodes = append(sub.nodes[:idx], sub.nodes[idx+1:]...)
		return true
	}
	return false
}

func (sub *SubNodes) String() string {
	strs := make([]string, len(sub.nodes))
	for i, node := range sub.nodes {
		strs[i] = node.String()
	}
	return fmt.Sprintf("[%s]", strings.Join(strs, ", "))
}

// ============================================================

type Composite struct{}

func (c Composite) walk(parentDir *Directory, path string) {
	fileInfos, err := os.ReadDir(path)
	if err != nil {
		return
	}

	for _, fileInfo := range fileInfos {
		fullPath := filepath.Join(path, fileInfo.Name())
		if fileInfo.IsDir() {
			dir := NewDirectory(fullPath, parentDir)
			parentDir.AddSubNode(dir)
			c.walk(dir, fullPath)
		} else {
			ff := &File{fullPath}
			parentDir.AddSubNode(ff)
		}
	}
}

func (c Composite) BuildFileSystemTree(basePath string) FileSystemNode {
	fileInfo, err := os.Stat(basePath)
	if os.IsNotExist(err) {
		return nil
	}
	if fileInfo.IsDir() {
		dir := NewDirectory(basePath, nil)
		c.walk(dir, basePath)
		return dir
	}
	return &File{basePath}
}
