package composite

import (
	"os/user"
	"testing"
)

func TestComposite(t *testing.T) {
	u, _ := user.Current()
	c := Composite{}
	fileSystemTree := c.BuildFileSystemTree(u.HomeDir + "/Downloads")
	t.Log(fileSystemTree)
}
