package com.tylor.evan.gamename123.Objects;

/**
 * ComputerUser
 *      models data for computer opponent tendancies
 */
public class ComputerUser extends User {

    public ComputerUser( String user_name,
                         String password,
                         String email,
                         String first_name,
                         String last_name,
                         String nickname,
                         int points,
                         boolean is_right_handed,
                         boolean is_human ){
        super( user_name,
                password,
                email,
                first_name,
                last_name,
                nickname,
                points,
                is_right_handed,
                is_human);


    }
}
