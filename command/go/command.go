package command

import "fmt"

// Button 命令执行者
type Button struct {
	command Command
}

func NewButton(command Command) *Button {
	return &Button{command}
}

func (b *Button) Press() {
	b.command.Execute()
}

// Command 命令
type Command interface {
	Execute()
}

// 设备命令（具体命令）
type deviceCommand func()

func (cmd *deviceCommand) Execute() {
	(*cmd)()
}

type (
	TurnOnCommand     = deviceCommand // 设备开机命令
	TurnOffCommand    = deviceCommand // 设备关机命令
	VolumeUpCommand   = deviceCommand // 设备音量+命令
	VolumeDownCommand = deviceCommand // 设备音量-命令
)

// Device 命令接收接口，抽象「设备」
type Device interface {
	TurnOn()     // 开机
	TurnOff()    // 关机
	VolumeUp()   // 音量 +
	VolumeDown() // 音量 -
}

// TV 具体「设备」：电视机
type TV struct {
	isRunning bool
	volume    uint8
}

func (tv *TV) TurnOn() {
	if !tv.isRunning {
		tv.isRunning = true
	}
	fmt.Println("Turning tv on")
}

func (tv *TV) TurnOff() {
	if tv.isRunning {
		tv.isRunning = false
	}
	fmt.Println("Turning tv off")
}

func (tv *TV) VolumeUp() {
	if tv.volume < 30 {
		tv.volume += 1
	}
	fmt.Println("Volume up to", tv.volume)
}

func (tv *TV) VolumeDown() {
	if tv.volume > 0 {
		tv.volume -= 1
	}
	fmt.Println("Volume down to", tv.volume)
}
