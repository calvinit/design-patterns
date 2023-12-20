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

type TurnOnCommand struct {
	device Device
}

func NewTurnOnCommand(device Device) *TurnOnCommand {
	return &TurnOnCommand{device}
}

func (c *TurnOnCommand) Execute() {
	c.device.TurnOn()
}

type TurnOffCommand struct {
	device Device
}

func NewTurnOffCommand(device Device) *TurnOffCommand {
	return &TurnOffCommand{device}
}

func (c *TurnOffCommand) Execute() {
	c.device.TurnOff()
}

type VolumeUpCommand struct {
	device Device
}

func NewVolumeUpCommand(device Device) *VolumeUpCommand {
	return &VolumeUpCommand{device}
}

func (c *VolumeUpCommand) Execute() {
	c.device.VolumeUp()
}

type VolumeDownCommand struct {
	device Device
}

func NewVolumeDownCommand(device Device) *VolumeDownCommand {
	return &VolumeDownCommand{device}
}

func (c *VolumeDownCommand) Execute() {
	c.device.VolumeDown()
}

// Device 命令接收接口
type Device interface {
	TurnOn()     // 开机
	TurnOff()    // 关机
	VolumeUp()   // 音量 +
	VolumeDown() // 音量 -
}

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
