package command

import "testing"

func TestCommand(t *testing.T) {
	tv := &TV{}

	turnOnCommand := NewTurnOnCommand(tv)
	turnOnButton := NewButton(turnOnCommand)
	turnOnButton.Press()

	volumeUpCommand := NewVolumeUpCommand(tv)
	volumeUpButton := NewButton(volumeUpCommand)
	volumeUpButton.Press()
	volumeUpButton.Press()
	volumeUpButton.Press()

	volumeDownCommand := NewVolumeDownCommand(tv)
	volumeDownButton := NewButton(volumeDownCommand)
	volumeDownButton.Press()
	volumeDownButton.Press()

	turnOffCommand := NewTurnOffCommand(tv)
	turnOffButton := NewButton(turnOffCommand)
	turnOffButton.Press()
}
