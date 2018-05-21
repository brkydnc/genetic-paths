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

  void draw() {
    for (Stick stick : brain.sticks) {
      stick.draw();
    }
  }

  Path clone() {
    Path path = new Path(this.start.copy(), this.end.copy(), this.brain.sticks[1].length, this.size);
    path.brain = this.brain.clone();
    return path;
  }
}
