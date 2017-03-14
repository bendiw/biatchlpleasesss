package printPackage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import mst.FileHandler;

public class PicPrinter {
	
	private ArrayList<Integer> neighborList;
	private FileHandler fh;
	
	public PicPrinter(ArrayList<Integer> neighborList, FileHandler fh) {
		this.neighborList = neighborList;
		this.fh = fh;
		run();
	}
	
	private void run() {
		List<List<Integer>> segments = setSegments();
		generateImage(segments, fh);
	}
	
	private void generateImage(List<List<Integer>> segments, FileHandler fh) {
		Color[][] pixels = fh.getPixels();
		int height = fh.getHeight();
		int width = fh.getWidth();
		int t = 0;
		Color green = new Color(0, 255, 0);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				List<Integer> segment = getSegment(segments, t);
				if (i != 0) {
					if (! segment.contains(t-width)) {
						pixels[i][j] = green;
					}
				}
				if (i != height-1) {
					if (! segment.contains(t+width)) {
						pixels[i][j] = green;
					}
				}
				if (j != 0) {
					if (! segment.contains(t-1)) {
						pixels[i][j] = green;
					}
				}
				if (j != width - 1) {
					if (! segment.contains(t+1)) {
						pixels[i][j] = green;
					}
				}
			}
		}
		fh.saveNewImage(pixels);
	}
	
	private List<Integer> getSegment(List<List<Integer>> segments, int t) {
		for (List<Integer> segment : segments) {
			if (segment.contains(t)) {
				return segment;
			}
		}
		return new ArrayList<Integer>();
	}
	
	private List<List<Integer>> setSegments() {
		List<List<Integer>> segments = new ArrayList<List<Integer>>();
		List<Integer> segment = new ArrayList<Integer>();
		List<Integer> visited = new ArrayList<Integer>();
		int next = 0;
		int old;
		while (visited.size() < neighborList.size()) {
			segment.add(next);
			visited.add(next);
			old = next;
			next = neighborList.get(old);
			if (segment.contains(next)) {
				segments.add(new ArrayList<Integer>(segment));
				segment.clear();
				next = setNext(visited);
				}
			else if (visited.contains(next)) {
				mergeSegments(segments, segment, next);
				segment.clear();
				next = setNext(visited);
			}
		}
		return segments;
	}
	
	private int setNext(List<Integer> visited) {
		int next;
		if (visited.size() == neighborList.size()) {
			next = 0;
		} else {
			next = ThreadLocalRandom.current().nextInt(0,neighborList.size());
			while (visited.contains(next)) {
				next = ThreadLocalRandom.current().nextInt(0,neighborList.size());
			}
		}
		return next;
	}
	
	private void mergeSegments(List<List<Integer>> segments, List<Integer> segment, int next) {
		for (List<Integer> s : segments) {
			if (s.contains(next)) {
				for (Integer integer : segment) {
					s.add(integer);
				}
				return;
			}
		}
	}
	
	public static void main(String[] args) {
	}
}
