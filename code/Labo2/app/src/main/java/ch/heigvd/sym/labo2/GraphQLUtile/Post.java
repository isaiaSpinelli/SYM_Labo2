package ch.heigvd.sym.labo2.GraphQLUtile;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

/* Classe permettant de representer un post */
public class Post {
    private String title;
    private String post;

    public Post(String title, String post) {
        this.title = title;
        this.post = post;
    }

    @Override
    public String toString() {
        return title +"\n \n"+ post;
    }
}
