# -*- coding: utf-8 -*-
"""
Created on Sun Oct  2 01:01:34 2016

@author: xuchunwei
"""

#输入字符串处理
class Buffer(object):
    def __init__(self, data):
        self.data = data
        self.offset = 0
        
    def peer(self):
        if self.offset >= len(self.data):
            return None
        return self.data[self.offset]
        
    def advance(self):
        self.offset += 1
        
#生成Token列表
class Token(object):
    def consume(self, buffer):
        pass


class IntToken(object):
    def consume(self, buffer):
        accum = ""
        ch = buffer.peer
        while True:
            if ch is None or ch not in "0123456789":
                break;
            else:
                accum += ch
                buffer.advance()
        if accum != "":
            return ("int", int(accum))
        else:
            return None
            
class OperatorToken(Token):
    def consume(self, buffer):
        ch = buffer.peek()
        if ch is not None and ch in "+-":
            buffer.advance()
            return ("ope", ch)
        return None

def tokenize(string):
    buffer = Buffer(string)
    tk_int = IntToken()
    tk_op = OperatorToken()
    tokens = []
    
    while buffer.peek():
        token  = None
        for tk in (tk_int, tk_op):
            token = tk.consume(buffer)
            if token:
                tokens.append(token)
                break
            
        if not token:
            raise ValueError("Error in syntax")
            
    return tokens

#表达二叉树的节点
class Node(object):
    pass

class IntNode(Node):
    def __init__(self, value):
        self.value = value        
            
class BinaryOpNode(Node):
    def __init__(self, kind):
        self.kind = kind
        self.left = None
        self.right = None

            