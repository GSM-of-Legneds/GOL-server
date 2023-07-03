package dev.yangsijun.gol.common.riot.common


class RiotApiUtil {
    companion object {
        val ASIA_HOST = "https://asia.api.riotgames.com"
        val KR_HOST = "https://kr.api.riotgames.com"
        val HEADER_NAME = "X-Riot-Token"
    }

    class Summoner {
        companion object {
            private val BASE_URL_PREFIX: String = "/lol/summoner/v4/summoners/"
            val BY_NAME_URL_PREFIX: String = this.BASE_URL_PREFIX+"by-name/"
            val BY_PUUID_URL_PREFIX: String = this.BASE_URL_PREFIX+"by-puuid/"
        }
    }

    class League {
        companion object {
            private val BASE_URL_PREFIX: String = "/lol/league/v4/"
            val BY_SUMMONER_ID_URL_PREFIX: String = this.BASE_URL_PREFIX+"entries/by-summoner/"
        }
    }

    class MatchIds {
        companion object {
            private val BASE_URL_PREFIX: String = "/lol/match/v5/matches/"
            val BY_PUUID_URL_PREFIX: String = this.BASE_URL_PREFIX+"by-puuid/"
            val BY_PUUID_URL_POSTFIX: String = "/ids"
        }
    }

    class Match {
        companion object {
            private val BASE_URL_PREFIX: String = "/lol/match/v5/matches/"
            val BY_MATCH_ID_PREFIX: String = this.BASE_URL_PREFIX
        }
    }
}
