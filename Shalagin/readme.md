#How to make it work
To work with the project [Flanagan's Scientific Library](http://www.ee.ucl.ac.uk/~mflanaga/java/flanagan.jar) and Java 1.8 are needed.

Before the start:
- Create *name.pmap* file. The file consists of several lines, each of them describes a planet or a spaceship. The structure of a line is **(x coord) (y coord) (z coord) (mass) (raduis) (x speed) (y speed) (z speed) (true or false : is that planet movable)**, all without brackets. In systems with large central body the central body is recommended to have coordinates (0, 0, 0) and to be immovable.
- Create *settings* file. It must contain one string: **(name of pmap file) (number of steps to emulate) (length of one step in seconds)**. 

Then launch Main class.

That will produce
- *.rep* file with information about launch results. To use it one can use Reader.readReport(name) method.
- *traj* directory with images of trajectories.