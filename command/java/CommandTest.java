/**
 * 对比策略模式和命令模式：
 * 1、在策略模式中，不同的策略具有相同的目的、不同的实现、互相之间可以替换；
 * 2、在命令模式中，不同的命令具有不同的目的，对应不同的处理逻辑，并且互相之间不可替换。
 */
public class CommandTest {
    public static void main(String[] args) {
        TV tv = new TV();

        TurnOnCommand turnOnCommand = new TurnOnCommand(tv);
        Button turnOnButton = new Button(turnOnCommand);
        turnOnButton.press();

        VolumeUpCommand volumeUpCommand = new VolumeUpCommand(tv);
        Button volumeUpButton = new Button(volumeUpCommand);
        volumeUpButton.press();
        volumeUpButton.press();
        volumeUpButton.press();

        VolumeDownCommand volumeDownCommand = new VolumeDownCommand(tv);
        Button volumeDownButton = new Button(volumeDownCommand);
        volumeDownButton.press();
        volumeDownButton.press();

        TurnOffCommand turnOffCommand = new TurnOffCommand(tv);
        Button turnOffButton = new Button(turnOffCommand);
        turnOffButton.press();
    }
}