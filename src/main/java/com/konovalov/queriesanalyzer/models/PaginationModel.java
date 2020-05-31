package com.konovalov.queriesanalyzer.models;

import java.util.ArrayList;
import java.util.List;

public class PaginationModel {

    private static final int NEXT_PAGES_MAX = 3;
    private static final int PREV_PAGES_MAX = 1;

    private final int totalPages;
    private final int curPageNum;
    private final List<Integer> nextPagesNums = new ArrayList<>();
    private final List<Integer> prevPagesNums = new ArrayList<>();
    private final boolean nextPagesSkipped;
    private final boolean prevPagesSkipped;

    public PaginationModel(int totalPages, int curPageNum) {
        this.totalPages = totalPages;
        this.curPageNum = curPageNum;

        for (int i = curPageNum; i < curPageNum + NEXT_PAGES_MAX; i++) {
            int nextPageNum = i + 1;
            if (nextPageNum < totalPages) nextPagesNums.add(nextPageNum);
            else break;
        }
        nextPagesSkipped = !nextPagesNums.isEmpty() && nextPagesNums.get(nextPagesNums.size() - 1) < totalPages - 1;

        for (int i = curPageNum; i > curPageNum - PREV_PAGES_MAX; i--) {
            int prevPageNum = i - 1;
            if (prevPageNum >= 0) prevPagesNums.add(prevPageNum);
            else break;
        }
        prevPagesSkipped = !prevPagesNums.isEmpty() && prevPagesNums.get(prevPagesNums.size() - 1) > 0;
    }

    public List<Integer> getNextPagesNums() {
        return nextPagesNums;
    }

    public List<Integer> getPrevPagesNums() {
        return prevPagesNums;
    }

    public boolean isNextPagesSkipped() {
        return nextPagesSkipped;
    }

    public boolean isPrevPagesSkipped() {
        return prevPagesSkipped;
    }

    public int getLastPageNum() {
        return totalPages - 1;
    }

    public int getCurPageNum() {
        return curPageNum;
    }

}
