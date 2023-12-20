// 命令执行者
class Button {
    private final Command command;

    public Button(Command command) {
        this.command = command;
    }

    public void press() {
        command.execute();
    }
}

// 命令
public interface Command {
    void execute();
}

class TurnOnCommand implements Command {
    private final Device device;

    public TurnOnCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOn();
    }
}

class TurnOffCommand implements Command {
    private final Device device;

    public TurnOffCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOff();
    }
}

class VolumeUpCommand implements Command {
    private final Device device;

    public VolumeUpCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.volumeUp();
    }
}

class VolumeDownCommand implements Command {
    private final Device device;

    public VolumeDownCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.volumeDown();
    }
}

interface Device {
    void turnOn(); // 开机

    void turnOff(); // 关机

    void volumeUp(); // 音量 +

    void volumeDown(); // 音量 -
}

class TV implements Device {
    private boolean isRunning;
    private int volume;

    @Override
    public void turnOn() {
        if (!isRunning) {
            isRunning = true;
        }
        System.out.println("Turning tv on");
    }

    @Override
    public void turnOff() {
        if (isRunning) {
            isRunning = false;
        }
        System.out.println("Turning tv off");
    }

    @Override
    public void volumeUp() {
        if (volume < 30) {
            ++volume;
        }
        System.out.println("Volume up to " + volume);
    }

    @Override
    public void volumeDown() {
        if (volume > 0) {
            --volume;
        }
        System.out.println("Volume down to " + volume);
    }
}