package dev.yangsijun.gol.common.common.enums.game

enum class MapType(val mapId: Int, val mapName: String, val notes: String) {
    CUSTOM(0, "Custom games", ""),
    SUMMONERS_RIFT_SUMMER_VARIANT(1, "Summoner's Rift", "Original Summer variant"),
    SUMMONERS_RIFT_AUTUMN_VARIANT(2, "Summoner's Rift", "Original Autumn variant"),
    THE_PROVING_GROUNDS(3, "The Proving Grounds", "Tutorial Map"),
    TWISTED_TREELINE_ORIGINAL(4, "Twisted Treeline", "Original Version"),
    THE_CRYSTAL_SCAR(8, "The Crystal Scar", "Dominion map"),
    TWISTED_TREELINE_LAST(10, "Twisted Treeline", "Last TT map"),
    SUMMONERS_RIFT_CURRENT(11, "Summoner's Rift", "Current Version"),
    HOWLING_ABYSS(12, "Howling Abyss", "ARAM map"),
    BUTCHERS_BRIDGE(14, "Butcher's Bridge", "Alternate ARAM map"),
    COSMIC_RUINS(16, "Cosmic Ruins", "Dark Star: Singularity map"),
    VALORAN_CITY_PARK(18, "Valoran City Park", "Star Guardian Invasion map"),
    SUBSTRUCTURE_43(19, "Substructure 43", "PROJECT: Hunters map"),
    CRASH_SITE(20, "Crash Site", "Odyssey: Extraction map"),
    NEXUS_BLITZ(21, "Nexus Blitz", "Nexus Blitz map"),
    CONVERGENCE(22, "Convergence", "Teamfight Tactics map")
}
