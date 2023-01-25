package agh.ics.oop;

public class Tower implements IMapElement{
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private int health;
    private int range;
    private int strength;
    public int type;
    public double maxHealth;

    public Tower(int health, int range, int strength, Vector2d lowerLeft, Vector2d upperRight, int type){
        this.health = health;
        this.range = range;
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.maxHealth = health;
        this.type = type;
        this.strength = strength;
    }

    public int getHealth(){
        return this.health;
    }

    public int getRange() { return this.range; }
    public Vector2d getLowerLeft(){
        return this.lowerLeft;
    }

    public Vector2d getUpperRight(){
        return this.upperRight;
    }

    public  Vector2d getUpperLeft() {
        return new Vector2d(this.lowerLeft.x, this.upperRight.y);
    }

    public  Vector2d getLowerRight() {
        return new Vector2d(this.upperRight.x, this.lowerLeft.y);
    }
    public void subtractHealth(int value){
        this.health -= value;
    }

    public int getStrength(){
        return this.strength;
    }

    public String toString(){
        return "t";
    }

    public int getPrice(){
        if (type == 1) return 300;
        else return 700;
    }

    public boolean isNextTo(Vector2d position){
        boolean left = position.x == this.getLowerLeft().x - 1 && position.y >= this.getLowerLeft().y && position.y <= getUpperLeft().y;
        boolean right = position.x == this.getLowerRight().x + 1 && position.y >= this.getLowerLeft().y && position.y <= getUpperLeft().y;
        boolean top = position.y == this.getUpperLeft().y + 1 && position.x >= this.getLowerLeft().x && position.x <= this.getLowerRight().x;
        boolean bottom = position.y == this.getLowerLeft().y - 1 && position.x >= this.getLowerLeft().x && position.x <= this.getLowerRight().x;
        return left || right || top || bottom;
    }
    public String getPath(IMapElement object) {
        if (this.type ==1 ) return "src/main/resources/tower.png";
        else return "src/main/resources/tower1.png";
    }

    public void addRange(){
        this.range += 1;
    }
    public int getCurrentPriceToSell(){
        return (int) Math.floor(((this.getHealth()/this.maxHealth) * this.getPrice()));
    }

    public Vector2d getPosition() {
        return this.getUpperLeft();
    }

    public double maxHealth() {
        return this.maxHealth;
    }
}
