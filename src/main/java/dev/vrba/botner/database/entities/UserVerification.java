package dev.vrba.botner.database.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user_verifications")
public class UserVerification
{
    // Discord id
    @DatabaseField(id = true)
    private long id;

    @DatabaseField
    private String verificationCode;

    @DatabaseField
    private boolean verified;

    public UserVerification()
    {
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setVerificationCode(String verificationCode)
    {
        this.verificationCode = verificationCode;
    }

    public void setVerified(boolean verified)
    {
        this.verified = verified;
    }

    public UserVerification(long id)
    {
        this.id = id;
        this.verificationCode = java.util.UUID.randomUUID().toString();
        this.verified = false;
    }

    public long getId()
    {
        return this.id;
    }

    public boolean isVerified()
    {
        return verified;
    }

    public String getVerificationCode()
    {
        return this.verificationCode;
    }
}
