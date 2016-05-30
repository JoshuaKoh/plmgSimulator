import random as r
import randomwrapy as rw

def roll2():
    return r.randint(1, 2)

def roll6():
    return r.randint(1, 6)
    # return rw.rnumlistwithreplacement(100, 1, 6)

# Takes a list of dicts (seq) and turns it into a dict of dicts with a key (key) which is in the dicts.
def build_dict(seq, key):
    return dict((d[key], dict(d, index=index)) for (index, d) in enumerate(seq))

'''
        "name": "",
        "text": "",
        "category": ""
'''