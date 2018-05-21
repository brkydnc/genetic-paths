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

  void draw() {
    fill(255, 0, 0);
    noStroke();
    ellipse(this.reachPoint.x, this.reachPoint.y, 20, 20);
    stroke(0);
    for (Path path : this.paths) { 
      path.draw();
    }
  }

  float calculateFitnessSum() {
    float sum = 0;
    for (Path path : this.paths) {
      sum += path.brain.calculateFitness();
    }
    return sum;
  }

  Path getAParent() {
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

  void mutate() {
    for (int i = 0; i < this.paths.length; i++) {
      this.paths[i].brain.mutate();
    }
  }

  void nextGeneration() {
    Path[] children = new Path[this.paths.length];
    for (int i = 0; i < children.length; i++) {
      children[i] = this.getAParent();
    }
    this.paths = children;
    this.generation += 1;
  }
  
  void check(){
    for(Path path : this.paths){
      if(path.brain.sticks[path.brain.sticks.length - 1].end.dist(this.reachPoint) < 10){
        path.brain.reached = true;
      }
    }
  }
}
