package prototype

import "testing"

func TestPrototype(t *testing.T) {
	AddGamePlayer("1", &GamePlayer{1, "张三", &Blood{111}})
	AddGamePlayer("2", &GamePlayer{2, "李四", &Blood{222}})
	AddGamePlayer("3", &GamePlayer{3, "王五", &Blood{333}})

	oldPlayers := GetGamePlayers()
	RefreshAllGamePlayers()
	newPlayers := GetGamePlayers()
	// newPlayers["3"].Blood.Volume = 123
	t.Logf("原型模式: old players=>%s, new players=>%s, old==new?=>%t\n",
		oldPlayers, newPlayers, &oldPlayers == &newPlayers)
}
