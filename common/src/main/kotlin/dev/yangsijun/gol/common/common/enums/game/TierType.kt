package dev.yangsijun.gol.common.common.enums.game

enum class TierType(val tierId: String) {
    CHALLENGER("CHALLENGER"),
    GRANDMASTER("GRANDMASTER"),
    MASTER("MASTER"),
    DIAMOND("DIAMOND"),
    PLATINUM("PLATINUM"),
    GOLD("GOLD"),
    SILVER("SILVER"),
    BRONZE("BRONZE"),
    IRON("IRON");

    override fun toString(): String {
        return tierId
    }
}
