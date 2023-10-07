package prototype

import (
	"fmt"
	"strings"
)

type Cloneable[T any] interface {
	Clone() T
}

type Blood struct {
	Volume int
}

func (b *Blood) Clone() *Blood {
	return &Blood{b.Volume}
}

func (b *Blood) String() string {
	return fmt.Sprintf("Blood{Volume=%d}", b.Volume)
}

type GamePlayer struct {
	ID    int
	Name  string
	Blood *Blood
}

func (g *GamePlayer) Clone() *GamePlayer {
	return &GamePlayer{
		ID:    g.ID,
		Name:  g.Name,
		Blood: g.Blood.Clone(), // 深拷贝（对于 GamePlayer 来说）
	}
}

func (g *GamePlayer) String() string {
	return fmt.Sprintf("GamePlayer{ID=%d, Name='%s', Blood=%v}", g.ID, g.Name, g.Blood)
}

type GamePlayers map[string]*GamePlayer

func (gs GamePlayers) Clone() GamePlayers {
	players := GamePlayers{}
	for key, player := range gs {
		players[key] = player // 浅拷贝，只拷贝了地址
	}
	return players
}

func (gs GamePlayers) String() string {
	var builder strings.Builder
	builder.WriteString("GamePlayers{")
	i := 0
	for key, player := range gs {
		if i > 0 {
			builder.WriteString(", ")
		}
		builder.WriteString(fmt.Sprintf("%s=%s", key, player.String()))
		i++
	}
	builder.WriteString("}")
	return builder.String()
}

var gamePlayers = GamePlayers{}

func AddGamePlayer(key string, player *GamePlayer) {
	gamePlayers[key] = player
}

func GetGamePlayers() GamePlayers {
	return gamePlayers
}

func RefreshAllGamePlayers() {
	newGamePlayers := gamePlayers.Clone()
	for key, player := range newGamePlayers {
		// 此处省略判断是否有数据更新的逻辑...
		newGamePlayers[key] = player.Clone()
		// delete(newGamePlayers, key)
	}
	gamePlayers = newGamePlayers
}
