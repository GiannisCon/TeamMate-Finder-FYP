package com.example.teammatefinderxo;

public class UserMatches {
    public String UserFullName, UserEmail, Gender, continent, FifaRanking, PubgRanking, LolRanking, Uuid, DiscordName, Avatar;

    public UserMatches(){ // Default constructor required for calls to DataSnapshot

    }
    public UserMatches(String UserFullName, String UserEmail,String Gender,String continent, String FifaRanking, String PubgRanking, String LolRanking, String Uuid, String DiscordName, String Avatar){
        this.UserEmail = UserEmail;
        this.UserFullName = UserFullName;
        this.Gender = Gender;
        this.continent = continent;
        this.FifaRanking = FifaRanking;
        this.PubgRanking = PubgRanking;
        this.LolRanking = LolRanking;
        this.Uuid = Uuid;
        this.Avatar = Avatar;
        this.DiscordName = DiscordName;
    }

}
