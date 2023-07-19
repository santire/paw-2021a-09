package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;

import java.util.Optional;

public interface TokenDao {

    /**
     *
     * @param token
     * @param user
     * @return
     */
    public VerificationToken create(String token, User user);

    /**
     *
     * @param tokenid
     */
    public void updateToken(final int tokenid);

    /**
     *
     * @param id
     * @return
     */
    public VerificationToken findById(long id);

    /**
     *
     * @param token
     * @return
     */
    public Optional<VerificationToken> findByToken(String token);

    /**
     *
     * @param uid
     * @return
     */
    public VerificationToken findByUId(long uid);

    /**
     *
     * @param token
     * @return
     */
    public String validateVerificationToken(String token);

    /**
     *
     * @param token
     * @return
     */
    public long getUser(String token);

    public void checkOut(VerificationToken token);
}