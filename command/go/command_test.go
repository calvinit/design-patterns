package command

import "testing"

func TestCommand(t *testing.T) {
	tv := &TV{}

	turnOnCommand := TurnOnCommand(tv.TurnOn)
	turnOnButton := NewButton(turnOnCommand)
	turnOnButton.Press()

	volumeUpCommand := VolumeUpCommand(tv.VolumeUp)
	volumeUpButton := NewButton(volumeUpCommand)
	volumeUpButton.Press()
	volumeUpButton.Press()
	volumeUpButton.Press()

	volumeDownCommand := VolumeDownCommand(tv.VolumeDown)
	volumeDownButton := NewButton(volumeDownCommand)
	volumeDownButton.Press()
	volumeDownButton.Press()

	turnOffCommand := TurnOffCommand(tv.TurnOff)
	turnOffButton := NewButton(turnOffCommand)
	turnOffButton.Press()
}
