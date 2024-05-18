package com.example.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class Rewards {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String taskCondition;
    private String Reward;


    public Rewards() {
    }

    public Rewards(Long Id, String taskCondition, String Reward) {
        this.Id = Id;
        this.taskCondition = taskCondition;
        this.Reward = Reward;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getTaskCondition() {
        return this.taskCondition;
    }

    public void setTaskCondition(String taskCondition) {
        this.taskCondition = taskCondition;
    }

    public String getReward() {
        return this.Reward;
    }

    public void setReward(String Reward) {
        this.Reward = Reward;
    }

    public Rewards Id(Long Id) {
        setId(Id);
        return this;
    }

    public Rewards taskCondition(String taskCondition) {
        setTaskCondition(taskCondition);
        return this;
    }

    public Rewards Reward(String Reward) {
        setReward(Reward);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Rewards)) {
            return false;
        }
        Rewards rewards = (Rewards) o;
        return Objects.equals(Id, rewards.Id) && Objects.equals(taskCondition, rewards.taskCondition) && Objects.equals(Reward, rewards.Reward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, taskCondition, Reward);
    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", taskCondition='" + getTaskCondition() + "'" +
            ", Reward='" + getReward() + "'" +
            "}";
    }


    
}
