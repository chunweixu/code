import turtle

def draw_square(some_turtle):
    for i in range(4):
        some_turtle.forward(100)
        some_turtle.right(90)

def draw_art():
    window = turtle.Screen()
    window.bgcolor("red")
   
    brad = turtle.Turtle()
    brad.shape("turtle")
    brad.color("green")
    brad.speed(6)
    for i in range(1,37):
        brad.right(10)
        draw_square(brad) 
    
   # angie = turtle.Turtle()
   # brad.shape("arrow")
   # angie.color("blue")
   # angie.speed(6)
   # angie.circle(100)

    window.exitonclick()

draw_art()
