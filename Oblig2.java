import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Collections;

class Oblig2 {

    public static void main(String[] args) {
        File movies = new File("movies.tsv");
        File actors = new File("actors.tsv");

        try {

            // Hashmap med movies(tt-id som nøkkel) og actors(nm-id som nøkkel)

            HashMap<String, Movie> m = lesMovies(movies);
            HashMap<String, Actor> a = lesActors(actors, m);

            // Hashmap som kobler sammen movies og alle skuespiller som har spilt i det
            // tt-id som nøkkel og aktuelle nm-ids i ArrayList
            HashMap<String, ArrayList<String>> ma = getMovieActor(m, a);

            // Grafen bygges ved hjelp av Hashmap "ma" som kobler sammen movies og actors,
            // og innlest HashMap actors
            Graph g = createGraph(ma, a, m);

            System.out.print("\n " + "Oppgave 1:" + "\n\n");
            System.out.println("Nodes: " + g.countNodes());
            System.out.println("Edges: " + g.countEdges());

            System.out.print("\n " + "Oppgave 2:" + "\n\n");

            // I Oppgave 2
            // Får java.lang.OutOfMemoryError:Java heap space" hvis jeg kjører alle
            // shortestPath-metode etter hverandre.
            // men funker hvis jeg kjører en og en.
            // Også, start- og slutt- Actor samsvarer med oppgaven men,
            // men nodene i midten er forkjellig.

            Oppgave2(g, m, a);

            System.out.println("\n " + "Oppgave 3:" + "\n");

            // av en eller annen grunn kan jeg ikke kjøre Oppgave2 og 3 samtidig,
            // må kommentere bort den ene
            Oppgave3(g, m, a);

            System.out.println("Oppgave 4: ");
            g.printComponents();

        } catch (FileNotFoundException e) {
            System.out.print("2");
        }
    }

    public static void Oppgave2(Graph g, HashMap<String, Movie> m, HashMap<String, Actor> a) {
        ArrayList<Actor> liste0 = g.shortestPath("nm2255973", "nm0000460", a);
        Collections.reverse(liste0);
        printShortestPath(liste0, a, m);

        // ArrayList<Actor> liste1 = g.shortestPath("nm0424060", "nm0000243", a);
        // Collections.reverse(liste1);
        // printShortestPath(liste1, a, m);

        // ArrayList<Actor> liste2 = g.shortestPath("nm4689420", "nm0000365", a);
        // Collections.reverse(liste2);
        // printShortestPath(liste2, a, m);

        // ArrayList<Actor> liste3 = g.shortestPath("nm0000288", "nm0001401", a);
        // Collections.reverse(liste3);
        // printShortestPath(liste3, a, m);

        // ArrayList<Actor> liste4 = g.shortestPath("nm0031483", "nm0931324", a);
        // Collections.reverse(liste4);
        // printShortestPath(liste4, a, m);

    }

    public static void Oppgave3(Graph g, HashMap<String, Movie> m, HashMap<String, Actor> a) {
        ArrayList<Actor> chilleste = g.dij("nm2255973", "nm0000460", a, m);
        Collections.reverse(chilleste);
        printChilleste(chilleste, a, m);

        ArrayList<Actor> chilleste1 = g.dij("nm0424060", "nm0000243", a, m);
        Collections.reverse(chilleste1);
        printChilleste(chilleste1, a, m);

        ArrayList<Actor> chilleste2 = g.dij("nm4689420", "nm0000365", a, m);
        Collections.reverse(chilleste2);
        printChilleste(chilleste2, a, m);

        ArrayList<Actor> chilleste3 = g.dij("nm0000288", "nm0001401", a, m);
        Collections.reverse(chilleste3);
        printChilleste(chilleste3, a, m);

        ArrayList<Actor> chilleste4 = g.dij("nm0031483", "nm0931324", a, m);
        Collections.reverse(chilleste4);
        printChilleste(chilleste4, a, m);
    }

    public static void printChilleste(ArrayList<Actor> path, HashMap<String, Actor> a, HashMap<String, Movie> movies) {

        for (int i = 0; i < path.size() - 1; i++) {
            Actor start = path.get(i);
            Actor neste = path.get(i + 1);
            System.out.print(start + "\n");
            ArrayList<String> startMovieListe = a.get(start.nm).movieIds;
            ArrayList<String> nesteMovieListe = a.get(neste.nm).movieIds;
            if (startMovieListe.retainAll(nesteMovieListe)) {
                System.out.print("=== [" + movies.get(startMovieListe.get(0)) + " ] ===> ");
            }
        }
        System.out.println(path.get(path.size() - 1) + " ");

    }

    public static void printShortestPath(ArrayList<Actor> path, HashMap<String, Actor> a,
            HashMap<String, Movie> movies) {

        for (int i = 0; i < path.size() - 1; i++) {
            Actor start = path.get(i);
            Actor neste = path.get(i + 1);
            System.out.print(start + "\n");
            ArrayList<String> startMovieListe = a.get(start.nm).movieIds;
            ArrayList<String> nesteMovieListe = a.get(neste.nm).movieIds;
            if (startMovieListe.retainAll(nesteMovieListe)) {
                System.out.print("=== [" + movies.get(startMovieListe.get(0)) + " ] ===> ");
            }
        }
        System.out.println(path.get(path.size() - 1) + " ");

    }

    public static Graph createGraph(HashMap<String, ArrayList<String>> moviesActors, HashMap<String, Actor> actors,
            HashMap<String, Movie> mov) {
        Graph graph = new Graph();

        // legge inn alle noder
        for (Map.Entry<String, Actor> actorEntry : actors.entrySet()) {
            String actorID = actorEntry.getKey();
            graph.addNode(actorID);
        }
        // legger inn alle kanter
        for (Map.Entry<String, ArrayList<Map.Entry<String, Double>>> entryNode : graph.getMap().entrySet()) {

            // for hver node i grafen - hent nodens movieId Liste
            String currentNode = entryNode.getKey(); // actor string
            ArrayList<Map.Entry<String, Double>> currentNabo = entryNode.getValue(); // naboer list <nm-id, rating>
            ArrayList<String> currentNodeMovieIds = actors.get(currentNode).getMovieIds(); // list <tt-id>

            ArrayList<String> naboKeys = new ArrayList<>();
            for (int i = 0; i < currentNabo.size(); i++) {
                naboKeys.add(currentNabo.get(i).getKey());
            }

            // for hver movieId i lista til node hvis finnes i movieActors
            for (String movieId : currentNodeMovieIds) { // hver tt-id i list <tt-id>
                if (moviesActors.keySet().contains(movieId)) {
                    ArrayList<String> naboer = moviesActors.get(movieId); // list <nm-id>
                    for (String nabo : naboer) { // String nm-id
                        if (!nabo.equals(currentNode) && !naboKeys.contains(nabo)) {
                            Double rat = mov.get(movieId).rating;
                            graph.addEdge(currentNode, nabo, rat);
                        }
                    }
                }
            }
        }
        return graph;
    }

    public static HashMap<String, ArrayList<String>> getMovieActor(HashMap<String, Movie> movies,
            HashMap<String, Actor> actors) {

        HashMap<String, ArrayList<String>> movieActorList = new HashMap<>();

        for (Map.Entry<String, Actor> actorEntry : actors.entrySet()) {
            String actorID = actorEntry.getKey();

            ArrayList<String> movieIds = actorEntry.getValue().getMovieIds();
            for (String m : movieIds) {
                if (movies.containsKey(m)) {
                    if (!movieActorList.containsKey(m)) {
                        ArrayList<String> newList = new ArrayList<>();
                        newList.add(actorID);
                        movieActorList.put(m, newList);
                    } else {
                        ArrayList<String> currentList = movieActorList.get(m);
                        currentList.add(actorID);
                    }
                }
            }
        }
        return movieActorList;
    }

    public static HashMap<String, Movie> lesMovies(File fil) throws FileNotFoundException {

        HashMap<String, Movie> movieList = new HashMap<>();
        try {
            Scanner scMovie = new Scanner(fil);
            while (scMovie.hasNext()) {
                String[] tokens = scMovie.nextLine().split("\t");
                String tt = tokens[0];
                String tittel = tokens[1];
                Double rating = Double.parseDouble(tokens[2]);
                movieList.put(tt, new Movie(tt, tittel, rating));
            }
        } catch (FileNotFoundException e) {
            System.out.println("0");
            throw e;
        }
        return movieList;
    }

    public static HashMap<String, Actor> lesActors(File fil, HashMap<String, Movie> mov) throws FileNotFoundException {

        HashMap<String, Actor> actorList = new HashMap<>();

        try {
            Scanner scActor = new Scanner(fil);
            while (scActor.hasNext()) {
                String[] tokens1 = scActor.nextLine().split("\t");
                String nm = tokens1[0];
                String navn = tokens1[1];
                ArrayList<String> filmer = new ArrayList<>();
                for (int i = 2; i < tokens1.length; i++) {
                    filmer.add(tokens1[i]);
                }
                actorList.put(nm, new Actor(nm, navn, filmer));
            }
        } catch (FileNotFoundException e) {
            System.out.println("1");
            throw e;
        }
        return actorList;
    }

}

// bruker HashMap for å bygge grafen
// nøkkel er en nm-id til Actor, og verdien er en ArrayList med nm-ids til
// naboer

class Graph {
    private Map<String, ArrayList<Map.Entry<String, Double>>> adjList;
    Set<ArrayList<String>> components = new HashSet<>();

    Graph() {
        this.adjList = new HashMap<String, ArrayList<Map.Entry<String, Double>>>();
    }

    // tar inn to noder, og legger hverandre i sine naboer
    void addEdge(String a1, String a2, Double rat) {

        adjList.get(a1).add(Map.entry(a2, rat));
        adjList.get(a2).add(Map.entry(a1, rat));
    }

    void addNode(String a) {
        adjList.putIfAbsent(a, new ArrayList<>());
    }

    double getRating(String a, String b) {
        ArrayList<Map.Entry<String, Double>> naboer = adjList.get(a);

        for (int i = 0; i < naboer.size(); i++) {
            if (naboer.get(i).getKey().contains(b)) {
                return naboer.get(i).getValue();
            }
        }
        return 0.0;
    }

    void printGraph() {
        adjList.forEach((key, value) -> System.out.print(key.toString() + " : " + value.toString() + "\n"));
    }

    Map<String, ArrayList<Map.Entry<String, Double>>> getMap() {
        return adjList;
    }

    int countNodes() {
        return adjList.size();
    }

    ArrayList<String> getnabostring(ArrayList<Map.Entry<String, Double>> l) {
        ArrayList<String> naboKeys = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            naboKeys.add(l.get(i).getKey());
        }
        return naboKeys;
    }

    ArrayList<String> geNaboActorsKeys(String s) {
        ArrayList<String> naboKeys = new ArrayList<>();
        for (int i = 0; i < adjList.get(s).size(); i++) {
            adjList.get(s).get(i).getKey();
            naboKeys.add(adjList.get(s).get(i).getKey());
        }
        return naboKeys;
    }

    int countEdges() {
        int sum = 0;
        for (Map.Entry<String, ArrayList<Map.Entry<String, Double>>> entry : adjList.entrySet()) {
            int current = entry.getValue().size();
            sum += current;
        }
        return sum / 2;
    }

    ArrayList<Actor> dij(String start, String slutt, HashMap<String, Actor> actors, HashMap<String, Movie> movies) {

        // priokø som lagrer avstand til start som nøkkel
        PriorityQueue<Edge> prioQ = new PriorityQueue<>();
        HashSet<String> visited = new HashSet<>();

        HashMap<Edge, String> distance = new HashMap<>(); // actor-id + edge
        ArrayList<Edge> p = new ArrayList<>();

        Edge startEdge = new Edge(actors.get(start), null, 0.0);
        prioQ.add(startEdge);
        distance.put(startEdge, start);

        while (!prioQ.isEmpty()) {
            Edge current = prioQ.poll();
            p.add(current);
            ArrayList<Map.Entry<String, Double>> naboliste = adjList.get(current.a1.nm); //
            for (int i = 0; i < naboliste.size(); i++) { // list: <nm-id, rating>
                String nabo = naboliste.get(i).getKey(); // nm-id
                if (nabo.equals(slutt)) {
                    double rating = 10 - getRating(current.a1.nm, nabo);
                    double cost = current.rating + rating;
                    Edge siste = new Edge(actors.get(nabo), current.a1, cost);
                    Double total = siste.rating;
                    System.out.print("Total weight: " + total + "\n");
                    distance.put(siste, start);
                    p.add(siste);

                    if (!prioQ.isEmpty()) {
                        prioQ.clear();
                    }

                    break;
                } else {
                    if (!visited.contains(nabo)) {
                        double rating = 10 - getRating(current.a1.nm, nabo);
                        double cost = current.rating + rating;
                        Edge neste = new Edge(actors.get(nabo), current.a1, cost);
                        prioQ.add(neste);
                        neste.prev = current;
                        visited.add(start);
                        visited.add(nabo);
                        distance.put(neste, start);
                        p.add(neste);

                    }
                }
            }
        }
        return getChillestePath(distance, actors.get(start), actors.get(slutt));
    }

    ArrayList<Actor> getChillestePath(HashMap<Edge, String> p, Actor st, Actor sl) {
        Set<Edge> edges = p.keySet();
        ArrayList<Actor> path = new ArrayList<>();
        while (sl != null) {
            for (Edge edge : edges) {
                if (edge.a1.equals(sl)) {
                    path.add(edge.a1);
                    sl = edge.a2;

                }
            }
        }

        return path;
    }

    // Oppgave 2
    // Bruker BFS for å traversere, men avbyter traversering når destinasjonen er
    // funnet
    // kaller på hjelpemetode getPath for å spore tilbake

    ArrayList<Actor> shortestPath(String start, String slutt, HashMap<String, Actor> actors) {
        ArrayList<Actor> besokt = new ArrayList<>();
        Deque<Actor> queue = new LinkedList<>();

        besokt.add(actors.get(start));
        queue.add(actors.get(start));

        while (!queue.isEmpty()) {
            Actor current = queue.poll();
            ArrayList<Map.Entry<String, Double>> naboliste = adjList.get(current.nm);

            for (int i = 0; i < naboliste.size(); i++) { // list <nm-id, rating>
                Actor nabo = actors.get(naboliste.get(i).getKey());
                if (nabo.equals(actors.get(slutt))) {
                    nabo.prev = current;
                    queue.clear();
                    break;
                } else {
                    if (!besokt.contains(nabo)) {
                        besokt.add(nabo);
                        queue.add(nabo);
                        nabo.prev = current;

                    }
                }

            }
        }
        return getPath(actors.get(slutt));
    }

    // sporer tilbake til starten og lager en sti
    public ArrayList<Actor> getPath(Actor slutt) {
        ArrayList<Actor> path = new ArrayList<>();
        while (slutt != null) {
            path.add(slutt);
            slutt = slutt.prev;
        }
        return path;
    }

    void printComponents() {
        Set<String> noder = adjList.keySet();
        bfs(noder);
        System.out.println("Komponenter totalt: " + components.size());
        System.out.print(components);

    }

    int connectedTotal() {
        return components.size();
    }

    // Oppgave 4

    // Finner komponentene i en mindre graf, men får stackoverflow feil når jeg
    // kjører det på grafen
    void bfs(Set<String> noder) {

        Set<String> besokt = new HashSet<>();
        Deque<String> queue = new LinkedList<>();
        // besokt.retainAll(noder);
        queue.add(noder.iterator().next());

        while (!queue.isEmpty()) {
            String s = queue.poll();
            ArrayList<Map.Entry<String, Double>> naboliste = adjList.get(s);
            for (int i = 0; i < naboliste.size(); i++) {
                if (!besokt.contains(naboliste.get(i).getKey())) {
                    besokt.add(naboliste.get(i).getKey());
                    queue.add(naboliste.get(i).getKey());
                }
            }
        }
        ArrayList<String> list = new ArrayList<>();
        list.addAll(besokt);
        components.add(list);
        Set<String> result = new HashSet<>();
        for (String s : besokt) {
            if (!noder.contains(s)) {
                result.add(s);
            }
        }
        for (String s : noder) {
            if (!besokt.contains(s)) {
                result.add(s);
            }
        }
        if (!result.isEmpty()) {
            bfs(result);
        }
    }
}

class Edge implements Comparable<Edge> {
    Actor a1;
    Actor a2;
    Edge prev;
    Movie movie;
    Double rating;

    public Edge(Actor a1, Actor a2, Double r) {
        this.a1 = a1;
        this.a2 = a2;
        this.rating = r;
        this.prev = null;
    }

    public Edge getPrev() {
        return this.prev;
    }

    @Override
    public String toString() {
        return this.a2 + " to: " + this.a1 + " Total weight: " + rating + "\n";
    }

    @Override
    public int compareTo(Edge e) {
        return this.rating.compareTo(e.rating);
    }
}

// node
class Actor {
    String nm;
    String navn;
    Actor prev; // peker til forrige node for å spore tilbake path
    ArrayList<String> movieIds;

    public Actor(String nm, String navn, ArrayList<String> f) {
        this.nm = nm;
        this.navn = navn;
        this.movieIds = f;
        this.prev = null;
    }

    public Actor getPrev() {
        return this.prev;
    }

    public String getActorID() {
        return nm;
    }

    public String getActorName() {
        return navn;
    }

    public ArrayList<String> getMovieIds() {

        return movieIds;
    }

    @Override
    public String toString() {
        return this.navn;
    }
}

// kant
class Movie {
    String tt;
    String navn;
    Double rating;
    ArrayList<Actor> actors;

    public Movie(String tt, String navn, Double r) {
        this.tt = tt;
        this.navn = navn;
        this.rating = r;
        this.actors = new ArrayList<>();
    }

    public String getMovieId() {
        return tt;
    }

    public String getMovieName() {
        return navn;
    }

    public Double getRating() {
        return rating;
    }

    public void addActor(Actor a) {
        actors.add(a);
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return this.navn + " (" + this.rating + ") ";
    }
}