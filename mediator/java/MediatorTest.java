public class MediatorTest {
    public static void main(String[] args) {
        Mediator mediator = new StationManager();

        PassengerTrain passengerTrain = new PassengerTrain(mediator);
        FreightTrain freightTrain = new FreightTrain(mediator);

        passengerTrain.arrive();
        freightTrain.arrive();
        passengerTrain.depart();
    }
}
