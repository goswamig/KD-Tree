GS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	StdOut.java\
	StdDraw.java\
	StdRandom.java\
	In.java\
	Point2D.java\
	PointSET.java\
	RectHV.java\
	KdTree.java\
	experiment.java\
	KdTreeVisualizer.java\
	RangeSearchVisualizer.java\
	AverageExperiment.java
	
	

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
