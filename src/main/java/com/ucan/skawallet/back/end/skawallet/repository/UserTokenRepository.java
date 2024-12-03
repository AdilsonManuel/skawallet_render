/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.repository;

import com.ucan.skawallet.back.end.skawallet.model.UserToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author azm
 */
@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long>
{

    Optional<UserToken> findByAccessToken(String token);
}
