PVector s, e;
Population pop;
boolean paused;
float scale = 0.01;
void setup() {
  size(800, 600);
  ellipseMode(CENTER);
  textSize(20);
  textAlign(BOTTOM, LEFT);

  s = new PVector(width/2, height/2);
  e = new PVector(width - 20, height-20);
  pop = new Population(s, e, 10, 100, 100);
}

void draw() {
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

void keyPressed() {
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
