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

  void update() {
    this.end = this.start.copy().add(this.prefix);
  }

  void draw() {
    line(this.start.x, this.start.y, this.end.x, this.end.y);
  }

  Stick clone() {
    Stick stick = new Stick(this.start.x, this.start.y, this.angle, this.length);
    return stick;
  }
}
