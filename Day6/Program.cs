using System.Drawing;
using Day6;

string[] fileContent = File.ReadAllLines("test.txt");
var board = new List<string>();
var startPosition = new Point(0, 0);
var foundStart = false;

foreach ((string line, int index) in fileContent.Select((line, index) => (line, index)))
{
    board.Add(line);
    if (!foundStart)
    {
        int linePosition = line.IndexOf('^');
        if (linePosition > 0)
        {
            startPosition = new Point(index, linePosition);
            foundStart = true;
        }
    }
}

var currentDirection = Direction.Up;
var visitedPointCount = 1;
var currentPoint = new Point(startPosition.X, startPosition.Y);
var visitedPoints = new HashSet<Point> { currentPoint };

while (true)
{
    Point nextPoint = NextPoint(currentPoint, currentDirection);
    if (!CheckInBounds(nextPoint, board.Count, board[0].Length))
        break;

    if (board[nextPoint.X][nextPoint.Y] == '#')
    {
        currentDirection = ChangeDirection(currentDirection);
        nextPoint = NextPoint(currentPoint, currentDirection);
    }

    currentPoint = nextPoint;

    if (!visitedPoints.Contains(currentPoint))
    {
        visitedPointCount++;
        visitedPoints.Add(currentPoint);
    }
}

Console.WriteLine($"Visited {visitedPointCount} positions");

// PART 2 - DOESN'T WORK

visitedPoints.Remove(startPosition);

var uniqueObstractions = 0;
var visitedPointsAfterCollision = new Dictionary<Point, Direction>();

foreach (var pointBlock in visitedPoints)
{
    currentDirection = Direction.Up;
    currentPoint = new Point(startPosition.X, startPosition.Y);
    var afterCollision = false;

    while (true)
    {
        Point nextPoint = NextPoint(currentPoint, currentDirection);
        if (!CheckInBounds(nextPoint, board.Count, board[0].Length))
            break;

        if (visitedPointsAfterCollision.TryGetValue(currentPoint, out Direction nextDirection) && currentDirection == nextDirection)
        {
            Console.WriteLine($"Obstacle at {pointBlock.X}, {pointBlock.Y}, found at {currentPoint.X}, {currentPoint.Y}");
            uniqueObstractions++;
            visitedPointsAfterCollision.Clear();
            break;
        }

        if ((nextPoint.X, nextPoint.Y) == (pointBlock.X, pointBlock.Y))
        {
            if (afterCollision)
            {
                visitedPointsAfterCollision.Add(currentPoint, currentDirection);
            }
            currentDirection = ChangeDirection(currentDirection);
            nextPoint = NextPoint(currentPoint, currentDirection);
            afterCollision = true;
        }

        else if (board[nextPoint.X][nextPoint.Y] == '#')
        {
            if (afterCollision)
            {
                visitedPointsAfterCollision.Add(currentPoint, currentDirection);
            }
            currentDirection = ChangeDirection(currentDirection);
            nextPoint = NextPoint(currentPoint, currentDirection);
        }

        currentPoint = nextPoint;
    }
}

Console.WriteLine($"{uniqueObstractions} unique obstractions");

return;

Point NextPoint(Point curPoint, Direction direction)
{
    int x = direction switch
    {
        Direction.Up => curPoint.X - 1,
        Direction.Down => curPoint.X + 1,
        Direction.Left => curPoint.X,
        Direction.Right => curPoint.X,
        _ => throw new ArgumentOutOfRangeException(nameof(direction), direction, null)
    };
    int y = direction switch
    {
        Direction.Up => curPoint.Y,
        Direction.Down => curPoint.Y,
        Direction.Left => curPoint.Y - 1,
        Direction.Right => curPoint.Y + 1,
        _ => throw new ArgumentOutOfRangeException(nameof(direction), direction, null)
    };

    return new Point(x, y);
}

bool CheckInBounds(Point curPoint, int sizeX, int sizeY) =>
    !(curPoint.X < 0 || curPoint.X >= sizeX || curPoint.Y < 0 || curPoint.Y >= sizeY);

Direction ChangeDirection(Direction direction) =>
    direction switch
    {
        Direction.Up => Direction.Right,
        Direction.Right => Direction.Down,
        Direction.Down => Direction.Left,
        Direction.Left => Direction.Up,
        _ => direction
    };
