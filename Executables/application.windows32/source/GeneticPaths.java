import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class GeneticPaths extends PApplet {

PVector s, e;
Population pop;
boolean paused;
float scale = 0.01f;
public void setup() {
  
  ellipseMode(CENTER);
  textSize(20);
  textAlign(BOTTOM, LEFT);

  s = new PVector(width/2, height/2);
  e = new PVector(width - 20, height-20);
  pop = new Population(s, e, 10, 100, 100);
}

public void draw() {
  if (!paused) {
    background(255);
    pop.draw();
    fill(255, 0, 0);
    text("Generation: "+ pop.generation, 10, height-10);
    
    if (frameCount % 1 == 0) {
      pop.nextGeneration();
      pop.mutate();
    }
  }
}

public void keyPressed() {
  if (keyCode == 32) {
    paused = !paused;
  }if (keyCode == 37) {
    pop.reachPoint.x -= 10;
  }if (keyCode == 38) {
    pop.reachPoint.y -= 10;
  }if (keyCode == 40) {
    pop.reachPoint.y += 10;
  }if (keyCode == 39) {
    pop.reachPoint.x += 10;
  }  
  //println(keyCode);
}
class Brain {
  Stick[] sticks;
  PVector start, end;
  boolean reached;

  Brain(PVector s, PVector e, int l, int size) {
    this.sticks = new Stick[size];  
    this.start = s;
    this.end = e;
    PVector counter = s.copy();
    for (int i = 0; i < this.sticks.length; i++) {
      this.sticks[i] = new Stick(counter.x, counter.y, random(360), l);
      counter.x = this.sticks[i].end.x;
      counter.y = this.sticks[i].end.y;
    }
  }

  public Brain clone() {
    Brain brain = new Brain(this.start.copy(), this.end, this.sticks[0].length, this.sticks.length);
    for (int i = 0; i < brain.sticks.length; i++) {
      brain.sticks[i] = this.sticks[i].clone();
    }
    return brain;
  }

  public float calculateFitness() {
    float fitness = 1 / this.sticks[this.sticks.length-1].end.dist(this.end);
    //float fitness = 0;
    //for(int i = 0; i < this.sticks.length; i ++){
    //  fitness += 1 / this.sticks[i].end.dist(this.end);
    //}
    if (this.reached) {
      fitness *= 500;
    }
    return fitness;
  }

  public void mutate() {
    if (!this.reached) {
      float mutateRatio = 0.01f;
      if (random(1) < mutateRatio) {
        this.sticks[0] = new Stick(this.start.x, this.start.y, random(360), this.sticks[0].length);
      }
      PVector counter = this.sticks[0].end.copy();
      for (int i = 1; i < this.sticks.length; i++) {
        if (random(1) < mutateRatio) {
          this.sticks[i] = new Stick(counter.x, counter.y, random(360), this.sticks[0].length);
        }
        this.sticks[i].start = this.sticks[i-1].end.copy();
        this.sticks[i].update();
        counter = this.sticks[i].end.copy();
      }
    }
  }
}
class Path {
  PVector start, end;  
  Brain brain;
  int size;

  Path(PVector s, PVector e, int l, int size) {
    this.brain = new Brain(s.copy(), e, l, size);
    this.start = s; 
    this.end = e;
    this.size = size;
  }

  public void draw() {
    for (Stick stick : brain.sticks) {
      stick.draw();
    }
  }

  public Path clone() {
    Path path = new Path(this.start.copy(), this.end.copy(), this.brain.sticks[1].length, this.size);
    path.brain = this.brain.clone();
    return path;
  }
}
class Population {
  Path[] paths;
  Path bestPath;
  PVector reachPoint;
  int generation;

  Population(PVector s, PVector e, int length, int totalSticksInAPath, int size) {
    this.paths = new Path[size];
    this.reachPoint = e;
    this.generation = 0;
    for (int i = 0; i < this.paths.length; i++) {
      this.paths[i] = new Path(s.copy(), e, length, totalSticksInAPath);
    }
  }

  public void draw() {
    fill(255, 0, 0);
    noStroke();
    ellipse(this.reachPoint.x, this.reachPoint.y, 20, 20);
    stroke(0);
    for (Path path : this.paths) { 
      path.draw();
    }
  }

  public float calculateFitnessSum() {
    float sum = 0;
    for (Path path : this.paths) {
      sum += path.brain.calculateFitness();
    }
    return sum;
  }

  public Path getAParent() {
    float random = random(this.calculateFitnessSum());
    float process = 0;

    for (int i =0; i < this.paths.length; i++) {
      process += this.paths[i].brain.calculateFitness();
      if (process > random) {
        return this.paths[i].clone();
      }
    }

    return null;
  }

  public void mutate() {
    for (int i = 0; i < this.paths.length; i++) {
      this.paths[i].brain.mutate();
    }
  }

  public void nextGeneration() {
    Path[] children = new Path[this.paths.length];
    for (int i = 0; i < children.length; i++) {
      children[i] = this.getAParent();
    }
    this.paths = children;
    this.generation += 1;
  }
  
  public void check(){
    for(Path path : this.paths){
      if(path.brain.sticks[path.brain.sticks.length - 1].end.dist(this.reachPoint) < 10){
        path.brain.reached = true;
      }
    }
  }
}
class Stick {
  PVector start, end, prefix;
  float angle;
  int length;

  Stick(float x, float y, float a, int l) {
    this.start = new PVector(x, y);
    this.end = new PVector(x + (sin(a*PI/180)*l), (y + cos(a*PI/180)*l));
    this.prefix = new PVector((sin(a*PI/180)*l), (cos(a*PI/180)*l));
    this.angle = a;
    this.length = l;
  }

  public void update() {
    this.end = this.start.copy().add(this.prefix);
  }

  public void draw() {
    line(this.start.x, this.start.y, this.end.x, this.end.y);
  }

  public Stick clone() {
    Stick stick = new Stick(this.start.x, this.start.y, this.angle, this.length);
    return stick;
  }
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "GeneticPaths" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
