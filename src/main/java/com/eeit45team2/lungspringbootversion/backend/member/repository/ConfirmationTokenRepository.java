package com.eeit45team2.lungspringbootversion.backend.member.repository;

import com.eeit45team2.lungspringbootversion.backend.member.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}