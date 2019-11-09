package ch.heigvd.sym.labo2.GraphQLUtile;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

/* Classe permettant de representer un auteur */
public class Authors {

    private int id;
    private String firstName, lastName;

    public Authors(int id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return  this.firstName + " " + this.lastName ;
    }
}
