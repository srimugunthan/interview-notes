# Python lists serve as dynamic arrays
arr = [10, 20, 30, 40, 50]

# Access - O(1)
print(arr[2])           # 30

# Insert at end - O(1) amortized
arr.append(60)

# Insert at index - O(n)
arr.insert(2, 25)       # [10, 20, 25, 30, 40, 50, 60]

# Delete by index - O(n)
arr.pop(2)              # removes 25

# Delete by value - O(n)
arr.remove(40)

# Search - O(n)
print(40 in arr)        # False (already removed)

print(arr)              # [10, 20, 30, 50, 60]
