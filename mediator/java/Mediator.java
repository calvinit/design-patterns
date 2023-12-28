import java.util.LinkedList;
import java.util.Queue;

// Train 列车接口（组件）
interface Train {
    void arrive();

    void depart();

    void permitArrival();
}

// PassengerTrain 旅客列车（具体组件）
class PassengerTrain implements Train {
    private final Mediator mediator;

    public PassengerTrain(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void arrive() {
        if (!mediator.canArrive(this)) {
            System.out.println("PassengerTrain: Arrival blocked, waiting");
            return;
        }
        System.out.println("PassengerTrain: Arrived");
    }

    @Override
    public void depart() {
        System.out.println("PassengerTrain: Leaving");
        mediator.notifyAboutDeparture();
    }

    @Override
    public void permitArrival() {
        System.out.println("PassengerTrain: Arrival permitted, arriving");
        arrive();
    }
}

// FreightTrain 货运列车（具体组件）
class FreightTrain implements Train {
    private final Mediator mediator;

    public FreightTrain(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void arrive() {
        if (!mediator.canArrive(this)) {
            System.out.println("FreightTrain: Arrival blocked, waiting");
            return;
        }
        System.out.println("FreightTrain: Arrived");
    }

    @Override
    public void depart() {
        System.out.println("FreightTrain: Leaving");
        mediator.notifyAboutDeparture();
    }

    @Override
    public void permitArrival() {
        System.out.println("FreightTrain: Arrival permitted, arriving");
        arrive();
    }
}

public interface Mediator {
    boolean canArrive(@SuppressWarnings("ClassEscapesDefinedScope") Train train);

    void notifyAboutDeparture();
}

class StationManager implements Mediator {
    private boolean isPlatformFree = true;
    private final Queue<Train> trainQueue = new LinkedList<>();

    @Override
    public boolean canArrive(Train train) {
        if (isPlatformFree) {
            isPlatformFree = false;
            return true;
        }
        trainQueue.offer(train);
        return false;
    }

    @Override
    public void notifyAboutDeparture() {
        if (!isPlatformFree) {
            isPlatformFree = true;
        }
        Train train = trainQueue.poll();
        if (train != null) {
            train.permitArrival();
        }
    }
}
