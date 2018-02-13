package ezequiel.ftpserver;

/**
 * Created by Ezequiel on 07/02/2018.
 */

public class UserDataBase{

    private String userName;
    private  String pass;
    private String permissions;
    private String path;

    public UserDataBase(String userName,String pass, String permissions, String path){

        this.userName = userName;
        this.pass = pass;
        this.permissions = permissions;
        this.path = path;

    }

    public String getUserName(){return userName;}
    public String getPassword(){return pass;}
    public String getPermission(){return permissions;}
    public String getPath(){return  path;}


}
