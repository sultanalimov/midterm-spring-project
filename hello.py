def deter_2x2(matrix):
    return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]

def deter_3x3(matrix):
    return (matrix[0][0] * matrix[1][1] * matrix[2][2] +
            matrix[0][1] * matrix[1][2] * matrix[2][0] +
            matrix[0][2] * matrix[1][0] * matrix[2][1] -
            matrix[0][2] * matrix[1][1] * matrix[2][0] -
            matrix[0][1] * matrix[1][0] * matrix[2][2] -
            matrix[0][0] * matrix[1][2] * matrix[2][1])

def deter_4x4(matrix):
    det = 0
    for c in range(4):
        submatrix = []
        for row in matrix[1:]:
            submatrix.append(row[:c] + row[c+1:])
        det += ((-1) ** c) * matrix[0][c] * deter_3x3(submatrix)
    return det

def solve_2x2(equation1, equation2):
    a1, b1, c1 = equation1  # a1*x + b1*y = c1
    a2, b2, c2 = equation2  # a2*x + b2*y = c2

    # Calculate the determinant
    det = a1 * b2 - a2 * b1

    if det == 0:
        # Check for infinite solutions or no solution
        if c1 * b2 == c2 * b1 and c1 * a2 == c2 * a1:
            return "Infinite solutions exist."
        else:
            return "No solution exists."

    x = (c1 * b2 - c2 * b1) / det
    y = (c1 - a1 * x) / b1

    return x, y

def get_matrix(san):
    matrix = []
    print(f"{san}x{san} Matrix:")
    for i in range(san):
        row = list(map(float, input(f"Row {i+1}: ").split()))
        matrix.append(row)
    return matrix

def main():
    print("Welcome to the Matrix and Equation Solver!")
    print("Choose an option:")
    print("1. Solve a system of equations (2x2)")
    print("2. Calculate matrix determinant (2x2, 3x3, 4x4)")
    choice = int(input("Enter your choice (1 or 2): "))

    if choice == 1:
        print("For 2x2 system of equations:")
        coefficients = []
        for i in range(2):
            coeffs = list(map(float, input(f"Enter coefficients for equation {i + 1} (a, b, c): ").split()))
            coefficients.append(coeffs)

        result = solve_2x2(coefficients[0], coefficients[1])
        if isinstance(result, str):
            print(result)  # Show type of solution
        else:
            print(f"Solution: x = {result[0]}, y = {result[1]}")

    elif choice == 2:
        print("Choose matrix size:")
        print("1. 2x2 Matrix")
        print("2. 3x3 Matrix")
        print("3. 4x4 Matrix")
        matrix_choice = int(input("Enter matrix size choice (1, 2, or 3): "))

        if matrix_choice == 1:
            matrix_2x2 = get_matrix(2)
            det = deter_2x2(matrix_2x2)
            print(f"Determinant of the 2x2 matrix: {det}")
        elif matrix_choice == 2:
            matrix_3x3 = get_matrix(3)
            det = deter_3x3(matrix_3x3)
            print(f"Determinant of the 3x3 matrix: {det}")
        elif matrix_choice == 3:
            matrix_4x4 = get_matrix(4)
            det = deter_4x4(matrix_4x4)
            print(f"Determinant of the 4x4 matrix: {det}")
        else:
            print("Error: Please choose 1, 2, or 3 for matrix size.")
    else:
        print("Invalid choice. Please select 1 or 2.")

if __name__ == "__main__":
    main()

