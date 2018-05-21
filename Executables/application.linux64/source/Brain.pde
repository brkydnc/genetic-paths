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

  Brain clone() {
    Brain brain = new Brain(this.start.copy(), this.end, this.sticks[0].length, this.sticks.length);
    for (int i = 0; i < brain.sticks.length; i++) {
      brain.sticks[i] = this.sticks[i].clone();
    }
    return brain;
  }

  float calculateFitness() {
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

  void mutate() {
    if (!this.reached) {
      float mutateRatio = 0.01;
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
