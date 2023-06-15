package com.example.teammatefinderxo;

public class User {
    public String UserFullName, UserEmail, Gender, continent, FifaRanking, PubgRanking, LolRanking,DiscordName,Avatar;


    public User(){ // Default constructor required for calls to DataSnapshot

    }
    public User(String UserFullName, String UserEmail,String Gender,String continent, String FifaRanking, String PubgRanking, String LolRanking, String DiscordName, String Avatar){
        this.UserEmail = UserEmail;
        this.UserFullName = UserFullName;
        this.Gender = Gender;
        this.continent = continent;
        this.FifaRanking = FifaRanking;
        this.PubgRanking = PubgRanking;
        this.LolRanking = LolRanking;
        this.DiscordName = DiscordName;
        this.Avatar = Avatar;
    }

}
