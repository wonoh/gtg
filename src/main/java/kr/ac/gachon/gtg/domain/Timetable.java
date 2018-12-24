package kr.ac.gachon.gtg.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Timetable {
    private ArrayList<Byte> table;
    private TimeMatrix timeMatrix;

    public Timetable(int size) {
        this.table = new ArrayList<>(Collections.nCopies(size, (byte) 0));
        this.timeMatrix = new TimeMatrix();
    }

    public Timetable(Timetable timetable) {
        this.table = new ArrayList<>(timetable.getTable());
        this.timeMatrix = new TimeMatrix(timetable.getTimeMatrix());
    }

    @Override
    public int hashCode() {
        return table.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this.table.size() != ((Timetable) obj).getTableSize()) {
            return false;
        }

        for (int i = 0; i < this.table.size(); i++) {
            Byte value = ((Timetable) obj).getTableValue(i);

            if (!this.table.get(i).equals(value)) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<Byte> getTable() {
        return this.table;
    }

    public Byte getTableValue(int index) {
        if (validateIndex(index)) {
            return this.table.get(index);
        }

        return (byte) -1;
    }

    public TimeMatrix getTimeMatrix() {
        return this.timeMatrix;
    }

    public int getTableSize() {
        return this.table.size();
    }

    public List<Integer> getIndexList() {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < getTableSize(); i++) {
            if (this.table.get(i) == 1) {
                list.add(i);
            }
        }

        return list;
    }

    private boolean validateIndex(int index) {
        return index >= 0 && index < this.table.size();
    }

    public boolean hasCourse(int index) {
        if (validateIndex(index)) {
            return table.get(index) == 1;
        }

        return false;
    }

    public boolean addCourse(int index) {
        if (hasCourse(index)) {
            return false;
        } else {
            this.table.set(index, (byte) 1);
            return true;
        }
    }

    public void removeCourse(int index) {
        this.table.set(index, (byte) 0);
    }

    @Override
    public String toString() {
        StringBuilder description = new StringBuilder("Timetable: ");

        for (int i = 0; i < getTableSize(); i++) {
            if (this.table.get(i) == 1) {
                description.append(i).append(" ");
            }
        }

        return description.toString();
    }
}
