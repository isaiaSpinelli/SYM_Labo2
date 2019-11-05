package ch.heigvd.sym.labo2.GraphQLUtile;

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
