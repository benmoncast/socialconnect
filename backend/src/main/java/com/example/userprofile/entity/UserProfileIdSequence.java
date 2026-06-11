package com.example.userprofile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile_id_sequences")
public class UserProfileIdSequence {

    @Id
    @Column(name = "date_key", length = 6, nullable = false)
    private String dateKey;

    @Column(name = "last_series", nullable = false)
    private Integer lastSeries;
}
