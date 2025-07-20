program Day06;

var
  Map: array[0..129, 0..129] of Char;
  Seen: array[0..129, 0..129] of Byte;

  Width, Height, StartX, StartY: Integer;

procedure Load(Name: String);
var
  T: Text;
  J: Integer;
  C: Char;
  S: String;
begin
  Width := 0;
  Height := 0;

  Assign(T, 'input.txt');
  Reset(T);
  
  while not Eof(T) do
  begin
    ReadLn(T, S);
    for J := 1 to Length(S) do
    begin
      C := S[J];
      if C = '^' then
      begin
        StartX := Height;
        StartY := I  - 1;
        C := '.';
      end;
      Map[Height, J - 1] := C;
      Height := Height + 1;
    end;
  end;

  Width := Length(S);

  Close(T);

  WriteLn('Size is ', Height, ' x ' , Width);
end;

procedure Dump;
var
  I, J: Integer;
begin
  for I := 0 to Height do
  begin
    for J := 0 to Width do
      if Seen[I][J] <> 0 then
        Write('*')
      else
        Write(Map[I][J]);
    WriteLn;
  end;
end;

function Patrol: Integer;
var
  I, J, Count, X, Y, DX, DY: Integer;
begin
  for I := 0 to Height do
    for J := 0 to Width do
      Seen[I][J] := 0;

  X := StartX;
  Y := StartY;
        
  while True do
  begin
    if Seen[X][Y] = 0 then
      Inc(Count);

            
        while (true) {
            // System.out.printf("x=%3d y=%3d dx=%2d dy=%2d\n", x, y, dx, dy);
            
            if (seen[x][y] == 0) {
                count++;
            }
                
            int k = dx == -1 ? 1 : dy == 1 ? 2 : dx == 1 ? 4 : 8;
            if ((seen[x][y] & k) != 0) {
                count = -1;
                break; // Been here, done that.
            }
            seen[x][y] |= k;
            
            int newX = x + dx;
            int newY = y + dy;
            
            if (newX < 0 || newY < 0 || newX >= height || newY >= width) {
                break;
            }
            
            if (map[newX][newY] != '.') {
                int tmp = dx;
                dx = dy;
                dy = -tmp;
            } else {
                x = newX;
                y = newY;
            }
        }
        
        return count;
    }
    
    void solve(String name) throws IOException {
        load(name);
        
        System.out.printf("startx=%d starty=%d\n\n", startX, startY);
        
        int part1 = patrol();
        dump();
        
        int part2 = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((i != startX || j != startY) && map[i][j] == '.') {
                    map[i][j] = 'O';
                    if (patrol() == -1) {
                        part2++;
                        dump();
                    }
                    map[i][j] = '.';
                }
            }
        }
        
        System.out.printf("\nPart 1: %10d\nPart 2: %10d\n\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.02 n/a ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}

