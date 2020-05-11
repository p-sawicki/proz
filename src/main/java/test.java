public boolean checkIfPathIsClear(Pair<short, short> start, Pair<short, short> destinaltion){ // checks if cells between start and destionation are not occupied
        Pair<short, short> startPos = start.getPosition();
        startX = start.getValue0();
        startY = start.getValue1();
        Pair<short, short> desPos = destination.getPosition();
        destX = destination.getValue0();
        destY = destination.getValue1();
        Cell[][] cells = start.boardptr.getCells();

        if(startX == destX){ // vertical movement
        destY > startY ? s=startY, d=destY : d=startY, s=destY;
        for(int k = s+1; k < d; k++){
        if(cells[k][destX].getOccupation() == 1)
        return 0;
        }
        }
        if(startY == destY){ // horizontal movement
        destX > startX ? s=startX, d=destX : d=startX, s=destX;
        for(int k = s+1; k < d; k++){
        if(cells[destY][k].getOccupation() == 1)
        return 0;
        }
        }
        if(startX != destX && startY != destY) { // diagonal movement
        if( (destX > startX && destY > startY) || (destX < startX && destY < startY) ) {
        destX > startX ? s = startX, d = destX :d = startX, s = destX; // s - lowest X, d - highest X
        destY > startY ? m = startY, t = destY :t = startY, m = destY; // m - lowest Y, t - highest Y
        for (int k = s + 1, int l = m + 1; k<d && l<t; k++, l++){ //increase X, increase Y
        if (cells[l][k].getOccupation() == 1)
        return 0;
        }
        }
        else{
        destX > startX ? s = startX, d = destX :d = startX, s = destX; // s - lowest X, d - highest X
        destY > startY ? m = startY, t = destY :t = startY, m = destY; // m - lowest Y, t - highest Y
        for (int k = s + 1, int l = t - 1; k<d && l>m; k++, l--){ //increase X, decrease Y
        if (cells[l][k].getOccupation() == 1)
        return 0;
        }
        }
        }
        return 1;
        }