import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Blood implements Cloneable {
    private int volume;

    public Blood(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        // return super.toString();
        return "Blood{volume=" + volume + "}";
    }
}

class GamePlayer implements Cloneable {
    private int id;
    private String name;
    private Blood blood;

    public GamePlayer() {

    }

    public GamePlayer(int id, String name, Blood blood) {
        this.id = id;
        this.name = name;
        this.blood = blood;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Blood getBlood() {
        return blood;
    }

    public void setBlood(Blood blood) {
        this.blood = blood;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        GamePlayer player = (GamePlayer) super.clone();     // 浅拷贝
        player.blood = (Blood) this.blood.clone();          // 递归克隆 blood 引用类型，对于 GamePlayer 来说是深拷贝（而不是仅仅克隆了 blood 的引用）
        return player;
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", blood=" + blood +
                '}';
    }
}

/**
 * 如果对象的创建成本比较大，而同一个类的不同对象之间差别不大（大部分字段都相同），
 * 在这种情况下，我们可以利用对已有对象（原型）进行复制（或者叫拷贝）的方式来创建新对象，以达到节省创建时间的目的。
 * 这种基于原型来创建对象的方式就叫作原型设计模式（Prototype Design Pattern），简称原型模式。
 */
@SuppressWarnings({"ClassEscapesDefinedScope", "unchecked"})
public class Prototype {
    public HashMap<String, GamePlayer> gamePlayers = new HashMap<>();

    public void addGamePlayer(String key, GamePlayer player) {
        gamePlayers.put(key, player);
    }

    public Map<String, GamePlayer> gamePlayers() {
        return gamePlayers;
    }

    /**
     * 浅拷贝 (shallow copy) 和深拷贝 (deep copy) 操作相结合，以节省对象创建时间和内存空间占用
     */
    public void refreshAll() {
        // 浅拷贝，类似 CopyOnWrite，整个 map 要么全部都是旧数据，要么全部都是新数据，去掉中间状态数据
        HashMap<String, GamePlayer> gamePlayers = (HashMap<String, GamePlayer>) this.gamePlayers.clone();
        Iterator<Map.Entry<String, GamePlayer>> playerIter = gamePlayers.entrySet().iterator();
        while (playerIter.hasNext()) {
            Map.Entry<String, GamePlayer> playerEntry = playerIter.next();
            try {
                // 此处省略判断是否有数据更新的逻辑...
                playerEntry.setValue((GamePlayer) playerEntry.getValue().clone());
            } catch (CloneNotSupportedException ignore) {
                playerIter.remove();
            }
        }
        this.gamePlayers = gamePlayers;
    }
}
