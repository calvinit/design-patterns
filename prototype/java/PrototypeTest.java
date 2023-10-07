import java.util.Map;

public class PrototypeTest {
    public static void main(String[] args) {
        Prototype prototype = new Prototype();
        prototype.addGamePlayer("1", new GamePlayer(1, "张三", new Blood(111)));
        prototype.addGamePlayer("2", new GamePlayer(2, "李四", new Blood(222)));
        prototype.addGamePlayer("3", new GamePlayer(3, "王五", new Blood(333)));

        Map<String, GamePlayer> oldPlayers = prototype.gamePlayers();
        prototype.refreshAll();
        Map<String, GamePlayer> newPlayers = prototype.gamePlayers();
        // newPlayers.get("3").getBlood().setVolume(123);

        System.out.printf("原型模式: old players=>%s, new players=>%s, old==new?=>%b%n",
                oldPlayers, newPlayers, oldPlayers == newPlayers);
    }
}
