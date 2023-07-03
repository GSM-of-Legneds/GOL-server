package dev.yangsijun.gol.common.riot.dto

data class LeagueApiResponse(
    val leagueId: String,
    val summonerId: String,
    val summonerName: String,
    val queueType: String, // RankedType
    val tier: String, // TierType
    val rank: String, // GameRankType
    val leaguePoints: Int,
    val wins: Int,
    val losses: Int,
    val veteran: Boolean,
    val freshBlood: Boolean,
    val hotStreak: Boolean
)
