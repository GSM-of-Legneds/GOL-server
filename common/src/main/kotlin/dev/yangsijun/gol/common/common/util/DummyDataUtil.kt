package dev.yangsijun.gol.common.common.util

import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.user.User
import gauth.GAuthUserInfo
import org.bson.types.ObjectId

class DummyDataUtil {

    companion object {
        val gauthInfo1: Map<String, Any> = mapOf(
            "email" to "s21041@gsm.hs.kr",
            "name" to "곽희상",
            "grade" to 3,
            "classNum" to 1,
            "num" to 1,
            "gender" to "male",
            "profileUrl" to "image55.png",
            "role" to "Student"
        )
        val user1Id: String = "5f6d775c29be48f7e50ea68e"
        val user1: User = User(
            id = ObjectId(user1Id),
            email = gauthInfo1["email"] as String,
            name = gauthInfo1["name"] as String,
            grade = gauthInfo1["grade"] as Int,
            classNum = gauthInfo1["classNum"] as Int,
            num = gauthInfo1["num"] as Int,
            gender = gauthInfo1["gender"] as String,
            profileUrl = gauthInfo1["profileUrl"] as String,
            role = gauthInfo1["role"] as String
        )

        val gauthInfo2: Map<String, Any> = mapOf(
            "email" to "s11011@gsm.hs.kr",
            "name" to "홍길동",
            "grade" to 3,
            "classNum" to 3,
            "num" to 3,
            "gender" to "female",
            "profileUrl" to "image3.png",
            "role" to "Student"
        )
        val user2Id: String = "64ade43154a38daea72fb205"
        val user2: User = User(
            id = ObjectId(user2Id),
            email = gauthInfo2["email"] as String,
            name = gauthInfo2["name"] as String,
            grade = gauthInfo2["grade"] as Int,
            classNum = gauthInfo2["classNum"] as Int,
            num = gauthInfo2["num"] as Int,
            gender = gauthInfo2["gender"] as String,
            profileUrl = gauthInfo2["profileUrl"] as String,
            role = gauthInfo2["role"] as String
            )

        val gauthInfo3: Map<String, Any> = mapOf(
            "email" to "s21011@gsm.hs.kr",
            "name" to "양시준-갱신전",
            "grade" to 3,
            "classNum" to 1,
            "num" to 13,
            "gender" to "male",
            "profileUrl" to "image123.png",
            "role" to "Student"
        )
        val user3Id: String = "64ade439a7d479d4d77d8955"
        val user3: User = User(
            id = ObjectId(user3Id),
            email = gauthInfo3["email"] as String,
            name = gauthInfo3["name"] as String,
            grade = gauthInfo3["grade"] as Int,
            classNum = gauthInfo3["classNum"] as Int,
            num = gauthInfo3["num"] as Int,
            gender = gauthInfo3["gender"] as String,
            profileUrl = gauthInfo3["profileUrl"] as String,
            role = gauthInfo3["role"] as String
        )

        val summoner1Id: String = "64adeb88d48b4515f4b2372f"
        val summoner1: Summoner = Summoner(  // 함
            id = ObjectId(summoner1Id),
            userId = user1.id!!,
            user = user1,
            summonerId = "DIiLDPb8BjQewHIbqm1adVUIAObCRiA-wHgAU7mKaGjRNgI",
            accountId = "Pkh25cyxBN_6RQF3qD9WZZ1azpFJj-cqWtsqpYEVe2zMz_g",
            puuid = "JRv9GZ1NllHPUY1DXqQZ66yWwbDNIdi8UDeOtW-4pFxPQMhr17Vc5x1yrhWFehSvyeP2sU3rWiSO2g",
            name = "골드1",
            profileIconId = 5799,
            revisionDate = 1689091968588,
            summonerLevel = 2501
        )

        val summoner11Id: String = "64adec69c24c7a26e20d0c4b"
        val summoner11: Summoner = Summoner(
            id = ObjectId(summoner11Id),
            userId = user1.id!!,
            user = user1,
            summonerId = "jpxQ_wKfc66bs13Ncj8HfLDkANVy2uyVTyANAtpdWqrY0z8",
            accountId = "CDZ2E-9mLzkXr-fnN_T2SwCibKyA5OqB7M74A5eELr4lxg4",
            puuid = "fblV11w6J5bxboTooHrB0e0NukO9LSjxcMkP_ZY69BYLTlwF-aJAq__t-zDH4LvkNx1RWpqIQJwoTw",
            name = "콩이아부지75",
            profileIconId = 4090,
            revisionDate = 1689117409190,
            summonerLevel = 1757
        )

        val summoner2Id: String = "64adec5ed768ec70da5cbbd8"
        val summoner2: Summoner = Summoner(
            id = ObjectId(summoner2Id),
            userId = user2.id!!,
            user = user2,
            summonerId = "smt3lj26070x4PWxbrcQeYQ0b-9kscR3i8BD4wib5v1FeQ",
            accountId = "9PzJa0hdLJNtPqOGLyVHBnZPIxplrOPh2NP8jpAx2bl8",
            puuid = "q_JuMhsYRfTD5BaJDWUB7StzGiTEeapgBMBrjzWsi5EiIEAS0_ab1UmG__mH3RjEQ2qioKsRjuYacw",
            name = "전 종일 게임해요",
            profileIconId = 5737,
            revisionDate = 1689092431256,
            summonerLevel = 2419
        )

        val summoner3Id: String = "64adec632b8bb481c1e19beb"
        val summoner3: Summoner = Summoner(
            id = ObjectId(summoner3Id),
            userId = user3.id!!,
            user = user3,
            summonerId = "yyzQKDHvNPiRzxpI8TtV7gplhMMQz6QvdZUNu6bTsGsuMAE",
            accountId = "GCaJ-N31uOPEPChwIG4R8M5favNRC6MMuYRdB5ZU1jlBnAc",
            puuid = "yguPQPhIKeX6mcqRFz9oohh9_xqvgFmMSEeOUPx9jSR5izZN8HVw9tZYuYf8uvhgnSqhN7g_I_vNMQ",
            name = "Aje CrazyZiggs",
            profileIconId = 5735,
            revisionDate = 1689093877135,
            summonerLevel = 1651
        )

        val users = listOf(user1, user2, user3)

        val summoners = listOf(summoner1, summoner11, summoner2, summoner3)
    }
}
