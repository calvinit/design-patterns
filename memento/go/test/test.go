package main

import (
	"fmt"
	"os"

	"design-patterns/memento/go"
)

// 单元测试不能使用 fmt.Scan 接受用户输入
func main() {
	inputText := &memento.InputText{}
	snapshotHolder := &memento.SnapshotHolder{}
	for {
		var input string
		fmt.Print("> ")
		if _, err := fmt.Scan(&input); err != nil {
			panic("input error: " + err.Error())
		}
		switch input {
		case ":list":
			fmt.Printf("=> %s\n", inputText.GetText())
		case ":undo":
			if snapshot := snapshotHolder.PopSnapshot(); snapshot != nil {
				inputText.RestoreSnapshot(snapshot)
			}
		case ":exit":
			os.Exit(0)
		default:
			snapshotHolder.PushSnapshot(inputText.CreateSnapshot())
			inputText.Append(input)
		}
	}
}
