#!/usr/bin/env python
from __future__ import print_function
import sys, re, os, copy
import getopt
import itertools
import json
import random
import logging
import math

logging.basicConfig(
    filename = "ir.log",
    format = "%(levelname)-10s %(asctime)s %(message)s",
    level = logging.INFO
)

log = logging.getLogger("core")

AllUV = ["Mode", "Temperature", "SwingV", "Fan", "OnOff"]
AdminUV = ["Mode", "OnOff"]
NormalUV = ["Temperature", "Fan"]
UVSpace = {
    "Mode":set([i for i in range(5)]),
    "Temperature":set([i for i in range(16, 31)]),
    "SwingV":set([0, 1]),
    "Fan":set([1, 2, 3, 6]),
    "OnOff":set([0, 1]),
}

class Top(object):
    def __init__(self):
        self.all_specs = []
        self.all_clusters = []
        self.all_irbs = []

        # Put some global initialization here
        random.seed()

top = Top()

class Options(object):
    def __init__(self):
        self.trace_bpa = False
        self.trace_bsa = False
        self.trace_len = False
        self.without_merge_specs = False

options = Options()

class BitSpec(object):

    ''' BitSpec is only used by UVSpec class when analyzing a UV's spec. It describes a data bit in the IR packet.

        Members:
        - bit_index: bit index in the IR packet
        - bit_value: a dictionary contains all possible bit values for each of possible UV values. bit value is 0 or 1 normally,
                     but it can be 0xFF which means implicit <TBD>
    '''

    def __init__(self):
        self.bit_index = 0
        self.bit_value = {}

    def SetIndex(self, index):
        self.bit_index = index

    def GetIndex(self):
        return self.bit_index

    def SetValue(self, uv_value, bv):
        ''' Set the bit value for specified UV value '''
        self.bit_value[uv_value] = bv

    def GetValue(self, uv_value):
        ''' Get the bit value for specified UV value '''
        return self.bit_value[uv_value]

def encode_uvspec(obj):

    ''' encode_uvspec is a function for dumping a UVSpec object into json string
    '''
    if not isinstance(obj, UVSpec):
        raise TypeError(repr(obj) + " is not JSON serializable")

    return { "Name": obj.name,
            "Conditions": obj.conditions,
            "Index": obj.list_of_index,
            "Keys": obj.uv_values,
            "Values": obj.uv_bits_in_list
            }

class UVSpec(object):   # Spec for one UV
    def __init__(self, name=None, conditions=None):
        # Name of the UV, i.e., one of the value from 'AllUV'
        self.name = name

        # Dict of bits (key = index, value = BitSpec object)
        # Dictionary bits is used when analyzing UV specs. After Finalize() is called, it can be discarded.
        self.bits = {}

        # List uv_values stores all possible values for this UV
        self.uv_values = []

        # Dict of (key = UV value, value = list of bits value)
        self.uv_bits_in_list = {}

        # The condition under which the UV spec can apply
        self.conditions = conditions

        # Index bits of this UV spec. It gets assigned after Finalize()
        self.list_of_index = None

    def SetBitSpec(self, index, bspec):
        self.bits[index] = bspec

    def GetBitSpec(self, index):
        try:
            return self.bits[index]
        except:
            return None

    def Finalize(self):

        # Making list_of_index
        l = []
        for b in self.bits:
            l.append(b)
        l.sort()
        self.list_of_index = l

        # Making bits_in_list
        for v in self.uv_values:
            self.uv_bits_in_list[v] = []
            for b in self.list_of_index:
                bit_value = self.bits[b].GetValue(v)
                self.uv_bits_in_list[v].append(bit_value)

    def ShowJson2(self):
        print(json.dumps(self.name))
        print(json.dumps(self.conditions))
        print(json.dumps(self.list_of_index))
        print(json.dumps(self.uv_values))
        print(json.dumps(self.uv_bits_in_list))


    def ShowJson(self):
        # Jsonize
        print("Json >>>")
        print(json.dumps(self, indent=4, separators=(',', ': '), default=encode_uvspec))
        print("<<<")

    def Dump(self, fp):
        # Dump json representation to a file
        json.dump(self, fp, indent=4, separators=(',', ': '), default=encode_uvspec)

    def Load(self, fp):
        obj = json.load(fp)
        self.name = obj["Name"]
        self.conditions = obj["Conditions"]
        self.list_of_index = obj["Index"]
        self.uv_values = obj["Keys"]
        self.uv_bits_in_list = obj["Values"]


    def Show(self):
        print("UV %s Spec when: " %self.name, end='')
        print(self.conditions)
        print("  Index: ", end='')

        for b in self.list_of_index:
            #print("%d " %self.bits[b].GetIndex(), end='')
            print("%d " %b, end='')
        print("")

        # Show bit value for each of the UV value
        for v in self.uv_values:
            print("  Key %d = " %v, end='')
            i = 0
            for b in self.list_of_index:
                #bit_value = self.bits[b].GetValue(v)
                bit_value = self.uv_bits_in_list[v][i]
                i += 1
                if bit_value == 0xff:
                    print("#", end='')
                elif bit_value == 0xfe:
                    print("-", end='')
                elif bit_value == 0xfd:
                    print("~", end='')
                else:
                    print("%d" %bit_value, end='')
            print("")
        print("")

    def IsValidSpec(self):
        # Valid spec means
        # No more than 1 implicit cell
        # Show bit value for each of the UV value

        least_bits = {
            "Mode":2,
            "Temperature":4,
            "SwingV":1,
            "Fan":2,
            "OnOff":1
        }

        if len(self.list_of_index) < least_bits[self.name]:
            return False

        nb_implicit_cell = 0
        for v in self.uv_values:
            #print("  Key %d = " %v, end='')
            implicit_cell = False
            for b in self.list_of_index:
                bit_value = self.bits[b].GetValue(v)
                if bit_value == 0xff:
                    implicit_cell = True
                    break

            if implicit_cell:
                nb_implicit_cell += 1

        # check if this spec only contains non-existing bits, virtual bits, or implicit bits
        nb_invalid_bits = 0
        for v in self.uv_values:
            for b in self.list_of_index:
                bit_value = self.bits[b].GetValue(v)
                if bit_value >= 0xfd:
                    nb_invalid_bits += 1

        if nb_invalid_bits == len(self.uv_values) * len(self.list_of_index):
            return False

        if nb_implicit_cell > 1:
            return False
        else:
            return True

    def IsMergableWith(self, spec):

        # Define: Set(A, v) = all the keys that are corresponding to value v, in spec A.
        # We say that spec A is mergable with spec B, if following conditions are satisfied:
        # - spec A and spec B are for the same UV
        # - for any value v that can be found in spec A, Set(B, v) is subset of Set(A, v)
        # Note that A and B can have different list_of_index, but B'list_of_index must be
        # subset of A'list_of_index

        if self.name != spec.name:
            return False

        si_self = set(self.list_of_index)
        si_spec = set(spec.list_of_index)
        if not si_spec.issubset(si_self):
            return False

        ## Now check each of values in self
        mergable = True
        for v in self.uv_values:
            y = self.uv_bits_in_list[v]
            l = []
            for w in self.uv_values:
                if y == self.uv_bits_in_list[w]:
                    l.append(w)

            set_for_self = set(l)

            # When self and spec have difference list of index, 'y' needs to be projected to 'spec'
            # using spec's list of index
            if si_spec.issubset(si_self):
                y = []
                for i, index in enumerate(self.list_of_index):
                    if index in spec.list_of_index:
                        y.append(self.uv_bits_in_list[v][i])

            l = []
            for w in spec.uv_values:
                if y == spec.uv_bits_in_list[w]:
                    l.append(w)

            set_for_spec = set(l)

            if not set_for_spec.issubset(set_for_self):
                mergable = False
                break

        return mergable

    def MergeTo(self, spec):
        c0 = self.conditions
        c1 = spec.conditions

        c = {}
        for uv in AllUV:
            s0 = UVSpace[uv]
            s1 = UVSpace[uv]
            if uv in c0:
                s0 = set(c0[uv])
            if uv in c1:
                s1 = set(c1[uv])

            if len(s0|s1) > 0 and (s0|s1) != UVSpace[uv]:
                c[uv] = tuple(s0|s1)

        spec.conditions = c


    def IsContainedIn(self, spec):
        # return True if self is contained in spec
        c0 = self.conditions
        c1 = spec.conditions

        c_result = True
        for name in AllUV:
            if not name in c1:
                continue
            elif not name in c0:
                c_result = False
                break
            else:
                for v in c0[name]:
                    if not v in c1[name]:
                        c_result = False
                        break

        if c_result == False:
            return False

        ## Compare bits spec

        list1 = self.list_of_index
        list0 = spec.list_of_index

        b_result = True
        for v in self.uv_values:
            #print("  Key %d = " %v, end='')
            for index in list0:
                if not index in list1:
                    b_result = False
                    break
                elif self.bits[index].GetValue(v) != spec.bits[index].GetValue(v):
                    ## both have the same bit, but the value is not the same
                    if self.bits[index].GetValue(v) < 254:
                        b_result = False
                        break

            if b_result == False:
                break

        return b_result


class IrPacket(object):
    def __init__(self):
        self.uv = {}
        self.uv["Mode"] = 0
        self.uv["OnOff"] = 0
        self.uv["Temperature"] = 0
        self.uv["Fan"] = 0
        self.uv["SwingV"] = 0
        self.raw_modsig = ""
        self.b = []
        self.repeated = False   # If repeated, it means it has the same bit stream with some other IRP
        self.unique = False

    def OnModsig(self, modsig):
        self.uv["Mode"] = int(modsig[0], 10)
        if modsig[1:3] == "**":
            self.uv["Temperature"] = 0
        else:
            self.uv["Temperature"] = int(modsig[1:3], 10)
        self.uv["SwingV"] = int(modsig[3], 10)
        self.uv["Fan"] = int(modsig[5], 10)
        self.uv["OnOff"] = int(modsig[6], 10)
        self.raw_modsig = modsig

    def OnMain(self, main):
        for i in main:
            self.b.append(int(i))

    def OnYhCode(self, yhcode):
        # YH Code is a captured-code from YH Board
        self.b.append(3)
        self.b.append(2)
        size = len(yhcode)
        i = 1
        while i < size:
            if yhcode[i] == '%':
                break
            byte = int(yhcode[i:i+2], 16)
            #print(byte)
            byte = (byte >> 4) | ((byte << 4) & 0xff)
            #print(byte, end='')
            #print(">>>>")
            #for j in range(7, -1, -1):
            for j in range(8):
                self.b.append(0)
                self.b.append((byte >> j) & 1)
                #print((byte >> j) & 1, end=' ')
            #print("<<<")
            i += 2

        self.b.append(0)

    def Show(self):
        print ("MODSIG %d%s%d0%d%d" %(
            self.uv["Mode"],
            "%02d" %self.uv["Temperature"] if self.uv["Temperature"] != 0 else "**",
            self.uv["SwingV"],
            self.uv["Fan"],
            self.uv["OnOff"]))

        print ("main ", end='')
        for i in self.b:
            print("%d" %i, end='')
        print("")

def pulse_diff(x, y):
    centor = (x + y)/2.0
    width = 300.0 + math.log(centor/2000.0, 10) * 1000.0
    if width < 300.0:
        width = 300.0
    tail = 25.0
    #print "pulse_diff [%d, %d], width %f, tail %f" %(x, y, width, tail)

    if abs(x-y) <= width:
        return 1.0
    else:
        return math.exp(-pow(abs(x - y) - width, 2)/(2.0*pow(tail, 2)))

def normalize(lengths):

    ''' This function returns normalized lengths table and a remap list '''

    lengths.sort()
    n = []
    for l in lengths:
        if not n:
            n.append([l])
        else:
            insert_to = -1
            for i, xs in enumerate(n):
                # xs is a list
                for x in xs:
                    d = pulse_diff(x, l)
                    #print d
                    if d >= 0.5:
                        insert_to = i
                        break

                if insert_to != -1:
                    break

            if insert_to == -1:
                n.append([l])
            else:
                n[insert_to].append(l)

    # generate remap
    remap = []
    for l in lengths:
        for i, nl in enumerate(n):
            if l in nl:
                remap.append(i)

    return ([lens[0] for lens in n], remap)



class IrBase(object):
    def __init__(self):
        self.device_id = ""
        self.irps = []
        self.lengths = []
        self.normalized_lengths = []    # Normalized means length values are regulated using a pre-defined tolerance, then sorted
        self.nb_pulses = 0 # nb_pulses means the maximum value of the pulses list, which can be called featured number of pulses.
        self.pulses = []    # list of all possible number of pulses
        self.max_bits = 0   # Maximum number of bits
        self.uv_possible_values = {}
        self.filename = ""
        for name in AllUV:
            self.uv_possible_values[name] = set()

    def extrace_id(self, value):
        pat = r".*?HD_(?P<id>\d+)"
        p = re.compile(pat)
        result = p.search(value)
        if result:
            return result.group("id")

    def sub_irb(self, nb_pulses):

        ''' This function returns a sub irb in which all irps in this
            sub irb have the same nb_pulses, i.e., size of IR bit stream.

            This function is used by pairing algorithm only.

        '''

        if not nb_pulses in self.pulses:
            return None

        irb = IrBase()
        if len(self.pulses) > 1:
            irb.device_id = self.device_id + "-" + str(nb_pulses)
        else:
            irb.device_id = self.device_id
        irb.lengths = self.lengths
        irb.normalized_lengths = self.normalized_lengths
        irb.nb_pulses = nb_pulses
        irb.pulses = [nb_pulses]
        irb.max_bits = nb_pulses

        ## Collect all the irps with requested number of pulses
        for irp in self.irps:
            if len(irp.b) - irp.b.count(0xfd) == nb_pulses:
                irb.irps.append(irp)

        # uv_possible_values is not calculated, as it is not used for pairing algorithm

        return irb

    def ProcessLine(self, line):

        patterns = [
            r"^(?P<name>Number of Pulse)\s*(\[.*?\])*\s*(?P<value>.+?)[\r\n]*$", # Matching Pulses
            r"^(?P<name>Pulse Quantity)\s*(\[.*?\])*\s*(?P<value>.+?)[\r\n]*$", # Matching Pulses
            r"^(?P<name>Lengths)\s+(?P<value>.+?)[\r\n]*$", # Matching Lengths
            r"^(?P<name>\S+?)\s+(?P<value>\S+?)[\r\n]*$",   # Matching MODSIG, main
        ]

        matched = False
        for pat in patterns:
            p = re.compile(pat)
            result = p.search(line)
            if result:
                matched = True
                break

        if not matched:
            return

        name = result.group("name")
        value = result.group("value")
        if name == "MODSIG":
            self.temp_irp = IrPacket()
            self.temp_irp.OnModsig(value)
            for uv_name in AllUV:
                if not (uv_name == "Temperature" and self.temp_irp.uv[uv_name] == 0):
                    self.uv_possible_values[uv_name].add(self.temp_irp.uv[uv_name])

        elif name == "main":
            self.temp_irp.OnMain(value)
            self.irps.append(self.temp_irp)
            if len(value) > self.max_bits:
                self.max_bits = len(value)

        elif name == "Lengths":
            self.lengths = [int(l, 10) for l in value.split()]
            if options.trace_len:
                print("Lengths %s" %self.lengths)

        if name == "Number of Pulse" or name == "Pulse Quantity":
            ''' value may be like "213", which is a single number,
                or
                "231, 115", a list of numbers
            '''
            self.pulses = [int(d, 10) for d in value.split(", ")]
            self.nb_pulses = max(self.pulses)

            if options.trace_len:
                print("Number of pulses", self.pulses)

        if name == "Device":
            self.device_id = self.extrace_id(value)

    def GetLengthVector(self):
        lv = {}
        lv["Name"] = self.device_id
        lv["Pulse"] = self.nb_pulses
        lv["Lengths"] = self.normalized_lengths
        return lv

    def GetConstantBits(self):
        ## Stability analysis for all irps
        ## Return a set including all the stable bits

        bv = []
        for i in range(self.max_bits):
            bv.append(0)

        for i in range(1, len(self.irps)):
            # compare (i-1)th and i-th
            for k in range(len(bv)):
                if self.irps[i-1].b[k] != self.irps[i].b[k]:
                    bv[k] = 1
        bi = []
        for i, v in enumerate(bv):
            if v == 0:
                bi.append(i)

        #print(set(bi))
        return set(bi)

    def GetFeaturedValue(self, featured_bits):
        # featured_bits is a set of bits
        # Return a list of bits that are corresponding to "featured_bits"
        fb = list(featured_bits)
        fb.sort()

        fv = []
        for i in fb:
            fv.append(self.irps[0].b[i])

        return fv

    # GetXXXRawIrp functions are only for test
    def GetRawIrp(self, n):
        irp = self.irps[n]
        r = RawIrp()
        r.lengths = self.normalized_lengths
        r.b = irp.b
        return r

    def GetRandomRawIrp(self):
        n = random.randrange(0, len(self.irps))
        return self.GetRawIrp(n)

    def ReadFile(self, filename):
        f = open(filename)
        line = f.readline()
        while line:
            self.ProcessLine(line)
            line = f.readline()
        f.close()
        self.Normalize()
        self.Entropy()

    # Use a tolerance to normalize the bit value
    def Normalize(self):

        self.normalized_lengths, index_remap = normalize(self.lengths)
        if options.trace_len:
            print(self.normalized_lengths)
            print("Remap index: %s" %index_remap)

        for irp in self.irps:
            for i in range(len(irp.b)):
                try:
                    irp.b[i] = index_remap[irp.b[i]]
                except KeyError as e:
                    raise ValueError("Out of range index found")

            ## Use 0xfd to indicates non-existing bit
            for j in range(len(irp.b), self.nb_pulses):
                irp.b.append(0xfd)

    def Normalize_old(self, tolerance):
        index_remap = {}
        index_remap[0] = 0
        lengths_n = []
        for i, l in enumerate(self.lengths):
            merge = True
            if not lengths_n:
                merge = False
            elif abs(lengths_n[-1] - l) > tolerance:
                merge = False

            if not merge:
                lengths_n.append(l)

            index_remap[i] = len(lengths_n) - 1

        # construct map
        #index_remap = {}
        #index_remap[0] = 0
        #for i in range(1, len(self.lengths)):
        #    for j in range(i):
        #        if abs(self.lengths[i] - self.lengths[j]) < tolerance:
        #            index_remap[i] = j
        #            break
        #    else:
        #        index_remap[i] = i

        self.normalized_lengths = lengths_n
        #print("Remap index: %s" %index_remap, lengths_n)
        if options.trace_len:
            print("Remap index: %s" %index_remap)

        for irp in self.irps:
            for i in range(len(irp.b)):
                try:
                    irp.b[i] = index_remap[irp.b[i]]
                except KeyError as e:
                    raise ValueError("Out of range index found")

        # Normalize the length table and save it into self.normalized_lengths
        #nl = {}
        #for m in index_remap:
        #    if index_remap[m] not in nl:
        #        nl[index_remap[m]] = []
        #    nl[index_remap[m]].append(self.lengths[m])

        #for m in nl:
        #    mean = sum(nl[m]) / len(nl[m])
        #    self.normalized_lengths.append(mean)

        #self.normalized_lengths.sort()
        if options.trace_len:
            print(self.normalized_lengths)

    def Entropy(self):
        e = 0
        for i in range(len(self.irps)):
            if self.irps[i].repeated:
                continue

            cnt = 0
            for j in range(i + 1, len(self.irps)):
                if self.irps[i].b == self.irps[j].b:
                    self.irps[j].repeated = True
                    cnt += 1

            if cnt == 0:
                e += 1
                self.irps[i].unique = True

        q = 0
        for i in range(len(self.irps)):
            if not self.irps[i].repeated:
                q += 1
        #print ("File: %s, Entropy = %d / %d, Quality = %d"  %(self.device_id, e, len(self.irps), q))

    def IrPacketSetForUV(self, uv_name, value):
        for i in self.irps:
            #if not i.unique:
            #   return

            #if (uv_name in NormalUV):
                #if i.uv["OnOff"] == 0:
                #   continue
                #if i.uv["SwingV"] == 1:
                #   continue
                #if not (i.uv["Mode"] == 1 or i.uv["Mode"] == 2):
                #   continue

            #if (uv_name == "Mode"):
            #   if i.uv["SwingV"] == 1:
            #       continue
            #   if i.uv["OnOff"] == 0:
            #       continue

            #if (uv_name == "SwingV"):
            #   if i.uv["OnOff"] == 0:
            #       continue

            if i.uv[uv_name] == value:
                yield i

# This class is used to mark if a bit is touched by a UV
class BitPrint(object):
    def __init__(self):
        self.touched_by = set()

    def TouchedBy(self, uv):
        self.touched_by.add(uv)

    def IsTouchedBy(self, uv):
        return uv in self.touched_by

class BitPositionAnalyzer(object):
    def __init__(self, irb):
        self.Analyze(irb)

    def Show(self):
        for name in AllUV:
            print("%s:\n  " %name, end='')
            for i in self.bit_fields[name]:
                    print("%d " %i, end='')
            print("\n")

    def CompareIrp(self, x, y):
        #if not x.unique or not y.unique:
        #   return

        if x.uv["Temperature"] == 0 or y.uv["Temperature"] == 0:
            return

        changed = 0
        k = ""
        for name in AllUV:
            if x.uv[name] != y.uv[name]:
                changed += 1
                k = name

        if changed != 1:
            return

        # Iterate all the bits
        for i in range(max(len(x.b), len(y.b))):
            ## check if this bit exists
            try:
                if x.b[i] != y.b[i]:
                    self.bit_print[i].TouchedBy(k)
            except IndexError as e:
                self.bit_print[i].TouchedBy(k)

    def Analyze(self, irb):
        # Initialize bit_print list
        self.bit_print = [BitPrint() for i in range(irb.max_bits)]

        nbCompared = 1
        while nbCompared < len(irb.irps):
            for i in range(nbCompared):
                self.CompareIrp(irb.irps[i], irb.irps[nbCompared])
            nbCompared += 1

        # Finalize
        self.bit_fields = {}
        for name in AllUV:
            l = []
            for b in self.bit_print:
                if b.IsTouchedBy(name):
                    idx = self.bit_print.index(b)
                    l.append(idx)
            self.bit_fields[name] = l

class BitStability(object):
    def __init__(self):
        self.valid = False
        self.bit_value = {} # dictionary, (value of the UV: bit value)
        self.variation = {} # dictionary, (value of the UV: variance)
        self.sum_of_variance = 0 # sum of all variations which indicates this bit is stable for all the possible UV value or not

class BitStabilityAnalyzer(object):
    ''' Bit stability analyzer. It outputs the UV specs.
        Bit stability analysis is done based on bit position analysis result.
    '''

    def __init__(self, irb, bpa):
        self.irb = irb
        self.bpa = bpa
        self.uv_specs = {}  # dict of uv specs (key = UV name, value = UVSpec object)
        self.final_specs = [] # Result specs are put into this list

        self.Analyze()

    def AnalyzeUVWithValue(self, uv, value, conditions):

        '''
            This function scans all the irps that satisfies the given conditions, and update self.bs.
            The operation set is defined as the irp set in which this analysis will be scanning.

        '''
        if options.trace_bsa:
            print("Analyzing %s for value %d" %(uv, value))

        #for irp in self.irb.IrPacketSetForUV("SwingV", value):
        for irp in self.irb.irps:
            # Evaluate this IRP against conditions
            if irp.uv[uv] != value:
                continue

            satisfied = True
            for k in conditions:
                s0 = False
                love = conditions[k]
                for x in love:
                    if irp.uv[k] == x:
                        s0 = True
                if not s0:
                    satisfied = False

            if not satisfied:
                continue

            if options.trace_bsa:
                print("M%d T%02d S%d F%d O%d: " %(
                    irp.uv["Mode"],
                    irp.uv["Temperature"],
                    irp.uv["SwingV"],
                    irp.uv["Fan"],
                    irp.uv["OnOff"]
                ), end='')

            for i in range(self.irb.max_bits):
                if not self.bpa.bit_print[i].IsTouchedBy(uv):
                    continue
                if options.trace_bsa:
                    print("%d "%irp.b[i], end='')

                self.bs[i].valid = True
                try:
                    bv = irp.b[i]
                except IndexError:
                    bv = -1

                if value not in self.bs[i].bit_value:
                    self.bs[i].bit_value[value] = bv
                    self.bs[i].variation[value] = 0
                else:
                    # compare the bit peviously stored here
                    if self.bs[i].bit_value[value] != bv: # bit changes
                        self.bs[i].variation[value] += abs(self.bs[i].bit_value[value] - bv)
                        self.bs[i].sum_of_variance += abs(self.bs[i].bit_value[value] - bv)

            if options.trace_bsa:
                print("")

    def AnalyzeUV(self, uv, conditions):

        ''' Analyze a UV under specified conditions. It updates the UVSpec object on self.uv_spec[uv].

            self.bs keeps track of all bits stability statistic. Each time when this function is
            call, self.bs object is re-initialized. self.bs is a list of BitStability object, each of which corresponds
            a bit in the irp.

            Definition: Analysis Data Set: ads(uv, value, conditions) = all irps which
            > irp.uv[uv] == value
            > Other irp's UV's values stasify 'conditions'

            Summary of a BitStability object:
            - self.bs[i]:                   i-th bit stability object, describing only i-th bit.
            - self.bs[i].valid:             True if bit i is touched by the UV under the conditions.
            - self.bs[i].bit_value:         A dictionary. The key is the UV's value, the value is the bit value of the first irp of the ADS.
            - self.bs[i].variation:         A dictionary. The key is the UV's value, the value is variation all i-th bit within the ADS.
            - self.bs[i].sum_of_variance:   sum of all bit vaiations of all discovered cell in the ADS. If it's 0, it indicates the corresponding bit is
                                            a full stable bit of the UV.

            Note that bit_value and variation may miss some of the cells of a UV, because ADS is only a subset of all irps.

        '''

        self.bs = [BitStability() for i in range(self.irb.max_bits)]
        self.uv_specs[uv] = None

        if options.trace_bsa:
            print("Conditions: ", end='')
            print(conditions)

        # The analysis is split into several cells. Each possible value of a UV is called a cell.
        for value in self.irb.uv_possible_values[uv]:
            self.AnalyzeUVWithValue(uv, value, conditions)

        # Now self.bs contains all stability statistic of the UV, under 'conditions'
        # Finalize the bit stability analysis result

        total_stable_bits = 0
        total_touched_bits= 0
        implicit_uv = False

        for b in self.bs:
            if b.valid:
                total_touched_bits += 1
                if b.sum_of_variance == 0:
                    # stable bit
                    total_stable_bits += 1

        if total_touched_bits == 0:
            # UV doesn't exist
            #print("UV %s: Non-exist\n" %uv)
            return

        elif total_stable_bits == 0:
            # Implicit UV, which means this UV doesn't have its own stable bits, but its control value
            # can be inferred from other UVs implicitly
            implicit_uv = True

        self.uv_specs[uv] = UVSpec(uv, conditions)

        # In this for loop, we create new BitSpec objects for stable or semi-stable bits.
        # BitSpec's data will be updated in next for loop.
        for b in self.bs:
            bit_spec = None
            if b.valid:
                if b.sum_of_variance == 0:
                    # stable bit
                    bit_spec = BitSpec()
                    #print ("%d " %self.bs.index(b), end='')

                else:
                    # b.sum_of_variance != 0 means that the bit is not fully stable, but it may be semi-stable, i.e., stable for some cells
                    semi_stable = 0 in [b.variation[k] for k in b.variation]

                    if implicit_uv and semi_stable:
                        bit_spec = BitSpec()

            if bit_spec != None:
                bit_spec.bit_index = self.bs.index(b)
                self.uv_specs[uv].SetBitSpec(self.bs.index(b), bit_spec)


        # In this for loop, we fill up the data of each BitSpec object.
        # The "data" are dictionary item: UV value: bit value

        for v in self.irb.uv_possible_values[uv]:
            v_has_bits = False

            stable_bits_for_key = 0
            for b in self.bs:
                if not b.valid:
                    continue

                bit_spec_value = -1
                try:
                    if implicit_uv:
                        var = b.variation[v]
                    else:
                        var = b.sum_of_variance
                    #var = b.variation[v]
                except KeyError:
                    var = -1   # -1 means that there is not variance statstics for this UV value v
                               # it imples that no irp was found in all ads's

                if var == 0:
                    # stable bit
                    stable_bits_for_key += 1

                    ## Note bit_value for v is not always existing
                    try:
                        if b.bit_value[v] == -1:
                            bit_spec_value = 254
                            raise RuntimeError("bit value can't be -1")
                        else:
                            bit_spec_value = b.bit_value[v]

                    except KeyError:
                        bit_spec_value = 254    # virtual bit, which means we didn't find any irp with this uv equal to this value.

                elif var == -1:
                    pass
                elif implicit_uv:
                    bit_spec_value = 255        # implicit bit

                if bit_spec_value != -1:
                    bspec_obj = self.uv_specs[uv].GetBitSpec(self.bs.index(b))
                    if bspec_obj:
                        bspec_obj.SetValue(v, bit_spec_value)
                        v_has_bits = True

            if v_has_bits:
                # Don't add possible UV value if there's no any bit corresponding to this value
                self.uv_specs[uv].uv_values.append(v)

        self.uv_specs[uv].Finalize()

    def IsInvalidImplicitUVSpec(self, spec):
        invalid = False

        ## First find the implicit cell
        implicit_cell_value = -1
        for v in spec.uv_values:
            list_v = spec.uv_bits_in_list[v]
            if 0xff in list_v:
                implicit_cell_value = v
                break

        if implicit_cell_value == -1:
            # Not an implicit UV
            return False

        for irp in self.irb.irps:

            # Test UV spec's condition, if not satisfied, do nothing
            # Spec's condition is like: {'Mode': (0, 1, 2, 4), 'SwingV': (0,), 'OnOff': (1,)}
            satisfied = True
            for c in spec.conditions:
                # c is name of UV
                if not irp.uv[c] in spec.conditions[c]:
                    satisfied = False
                    break

            if not satisfied:
                continue

            if irp.uv[spec.name] != implicit_cell_value:
                continue    # Only check the irp that belongs to implicit cell

            # Construct a list containing all the bits in the IRP
            irp_bits = []
            for b in spec.list_of_index:
                index = spec.bits[b].GetIndex()
                irp_bits.append(irp.b[index])

            nb_matched = 0
            for v in spec.uv_values:
                list_v = spec.uv_bits_in_list[v]
                if not 0xff in list_v:
                    # non-implicit cell
                    if irp_bits == list_v:
                        nb_matched += 1
                        break

            if nb_matched > 0:
                invalid = True
                break

        return invalid

    def Verify(self):
        result = {}
        for irp in self.irb.irps:
            for uv in AllUV:
                result[uv] = -1
                uv_spec = self.uv_specs[uv]

                index_table = uv_spec.list_of_index

                ##
                bit_table_irp = []
                for pos in index_table:
                    bit_table_irp.append(irp.b[pos])

                ## Check each of UV value
                for v in uv_spec.uv_values:
                    bit_table_uv = []
                    for b in index_table:
                        bit_table_uv.append(uv_spec.bits[b].GetValue(v))
                    if bit_table_uv == bit_table_irp:
                        result[uv] = v
                        break

            if irp.uv["Mode"] != result["Mode"] and \
                irp.uv["OnOff"] != result["OnOff"] or \
                irp.uv["Fan"] != result["Fan"] or \
                irp.uv["SwingV"] != result["SwingV"] or \
                irp.uv["Temperature"] != 0 and irp.uv["Temperature"] != result["Temperature"]:
                #print("\nVerify failed")
                for uv in AllUV:
                    print("%d " %irp.uv[uv], end='')
                print("==> ", end='')
                for uv in AllUV:
                    print("%d " %result[uv], end='')
                print("")
            #else:
            #   print("OK")

    def AllConditions(self, uv):
        if uv == "Temperature" or uv == "Fan":
            all_values = ["O0", "O1", "S0", "S1", "M0", "M1", "M2", "M3", "M4",]
        elif uv == "OnOff":
            all_values = ["S0", "S1",]
        elif uv == "SwingV":
            all_values = ["O0", "O1",]
        else:
            all_values = ["O0", "O1", "S0", "S1",]

        all_conds = []
        for n in range(1, len(all_values)+1):
            all_conds += list(itertools.combinations(all_values, n))

        yield {}

        for c in all_conds:
            m = ()
            s = ()
            o = ()
            for elem in c:
                if elem[0] == 'O':
                    o += (int(elem[1:]),)
                elif elem[0] == 'M':
                    m += (int(elem[1:]),)
                elif elem[0] == 'S':
                    s += (int(elem[1:]),)

            if len(m) == 5:
                continue
            if len(s) == 2:
                continue
            if len(o) == 2:
                continue

            #print(m)
            #print(s)
            #print(o)
            cond = {}
            if len(m) > 0:
                cond["Mode"] = m
            if len(s) > 0:
                cond["SwingV"] = s
            if len(o) > 0:
                cond["OnOff"] = o

            yield cond


    def Analyze(self):
        for name in AllUV:
            self.AnalyzeUVEx(name)

    def AnalyzeUVEx(self, name):
        #for name in AllUV:
        #   self.AnalyzeUV(name)
        #for name in AllUV:
        #   self.uv_specs[name].Show()

        #all_conds = [
        #   ("Mode", 0),
        #   ("Mode", 0),
        #   ("Mode", 0, 1, 2),
        #   ("Mode", 0, 1, 2),
        #   ("Mode", 0, 1, 2),
        #]

        all_specs = []

        count = 0
        for cond in self.AllConditions(name):
            #name = "Temperature"
            #name = "Mode"
            #print(cond)

            self.AnalyzeUV(name, cond)
            try:
                if self.uv_specs[name].IsValidSpec():
                    #print(">>> %dth spec" %count)
                    count += 1
                    #print(cond)
                    #self.uv_specs[name].Show()
                    all_specs.append(self.uv_specs[name])
            except:
                pass

        ## Examine all spec
        mark = list()
        range_all = range(len(all_specs))
        for i in range_all:
            mark.append(-1)

        for i in range_all:
            for j in range_all:
                if i == j:
                    continue

                if all_specs[i].IsContainedIn(all_specs[j]):
                    mark[i] = j

        #print(mark)
        to_be_merged = []
        for i in range_all:
            if mark[i] == -1:
                #all_specs[i].Show()
                all_specs[i].Finalize()

                if not self.IsInvalidImplicitUVSpec(all_specs[i]):
                    to_be_merged.append(all_specs[i])

        list_of_specs_index = [i for i in range(len(to_be_merged))]

        if not options.without_merge_specs:
            # Merge specs if we can

            while True:
                some_specs_merged = False
                for i in list_of_specs_index:
                    for j in list_of_specs_index:
                        if i == j or i == -1 or j == -1:
                            continue
                        if to_be_merged[i].IsMergableWith(to_be_merged[j]):
                            to_be_merged[i].MergeTo(to_be_merged[j])
                            list_of_specs_index[i] = -1
                            some_specs_merged = True
                            #print(name, " %d --> %d"%(i,j))

                if not some_specs_merged:
                    break

        for i in list_of_specs_index:
            if i != -1:
                self.final_specs.append(to_be_merged[i])

    def Show(self):
        print("\n%s: All UV Specs:" %self.irb.device_id)
        for s in self.final_specs:
            s.Show()

class IrpSolver(object):
    def __init__(self, all_specs):
        self.solved = {}  # dict: {"Mode": 0, }
        self.unsolved = [] # list: ["Mode", "SwingV",]
        for uv in AllUV:
            self.unsolved.append(uv)

        self.multi_valued = {} # dict: {"Mode": (0,1,2), }
        self.intermediate = False
        self.all_specs = all_specs

    def IsAllSolved(self):
        return len(self.solved) == 5 or len(self.unsolved) == 0

    def Duplicate(self):
        return copy.deepcopy(self)

    def Copy(self, solver):
        self.solved = copy.deepcopy(solver.solved)
        self.unsolved = copy.deepcopy(solver.unsolved)
        self.multi_valued = copy.deepcopy(solver.multi_valued)

    def Show(self):
        print("Result: ", end='')
        print(self.solved)
        if len(self.unsolved) > 0:
            print("Unsolved: ", end='')
            print(self.unsolved)
        if len(self.multi_valued) > 0:
            print("Multi-valued: ", end='')
            print(self.multi_valued)

    def Solve(self, irp, spec):
        ## Return a tuple
        ##   Nothing:       ('Mode', ())
        ##   Multi-valued:  ('Mode', (0, 1, 2, 4))
        ##   Single-valued: ('Mode', (0))

        ## If a UV is already solved, or multi-valued, return "Nothing"

        # Solve an Irp, given the spec, update result to uv
        #print("Checking UV %s, against Spec: "%spec.name, end='')
        #print(spec.conditions)

        result = (spec.name, ())

        if spec.name in self.solved:
            # It has already been solved
            return result

        uv = {}
        for n in AllUV:
            uv[n] = -1

        for s in self.solved:
            uv[s] = self.solved[s]

        # Test UV spec's condition, if not satisfied, do nothing
        # Spec's condition is like: {'Mode': (0, 1, 2, 4), 'SwingV': (0,), 'OnOff': (1,)}
        satisfied = True
        for c in spec.conditions:
            # c is name of UV
            if not uv[c] in spec.conditions[c]:
                satisfied = False
                break

        if not satisfied:
            return result

        # Construct a list containing all the bits in the IRP
        irp_bits = []
        for index in spec.list_of_index:
            #index = spec.bits[b].GetIndex()
            #assert index == b
            irp_bits.append(irp.b[index])
            # print("%d "%index, end='')

        #print("")
        #print(irp_bits)

        list_matched = []
        implicit_cell = -1
        for v in spec.uv_values:
            list_v = spec.uv_bits_in_list[v]

            ## Check if this cell is implicit
            all_sharps = True
            for b in list_v:
                if b != 0xff:
                    all_sharps = False
            if all_sharps:
                implicit_cell = v

            if irp_bits == list_v:
                list_matched.append(v)

        if len(list_matched) == 0 and implicit_cell != -1:
            list_matched.append(implicit_cell)

        # if result is multi-valued, and the result we are about to give
        # is exactly the same with before, we return nothing
        if len(list_matched) > 1:
            if spec.name in self.multi_valued:
                if set(self.multi_valued[spec.name]).issubset(set(list_matched)):
                    return (spec.name, ())

        return  (spec.name, tuple(list_matched))
        #print(spec.conditions)


    def Update(self, result):
        # Update self from result
        #print("Updating ", end='')
        #print(result)

        if len(result[1]) == 0:
            return

        if len(result[1]) == 1:
            # Add to solved
            self.solved[result[0]] = result[1][0]
            if result[0] in self.multi_valued:
                del self.multi_valued[result[0]]
        elif len(result[1]) > 1:
            # Add to multi-valued
            self.multi_valued[result[0]] = result[1]

        if result[0] in self.unsolved:
            self.unsolved.remove(result[0])


    def EnumerateMultiValue(self):
        lol = []
        for u in self.multi_valued:
            l = []
            for v in self.multi_valued[u]:
                l.append((u, (v,)))
            lol.append(l)

        nb_variables = 0
        limits = []
        for i in lol:
            nb_variables += 1
            limits.append(len(i))
            #print i
        if nb_variables == 0:
            return []

        #print("Limits: ", end='')
        #print(limits)

        counter = []
        for i in range(nb_variables):
            counter.append(0)

        result = []
        while True:
            # for current counter
            l = []
            for i in range(len(counter)):
                l.append(lol[i][counter[i]])
            #print l
            result.append(l)

            # now increase counter
            counter[0] += 1
            overflow = False
            try:
                for i in range(len(counter)):
                    if counter[i] >= limits[i]:
                        counter[i] = 0
                        counter[i+1] += 1
            except:
                overflow = True

            if overflow:
                break;

            #print "counter"
            #print counter

        #print "Result"
        #for i in result:
        #   print i

        return result

    def SolveIrp(self, irp):
        # Return a list of IRP solutions

        while True:
            # Iterate on all specs
            do_nothing = True
            for spec in self.all_specs:
                result = self.Solve(irp, spec)
                if len(result[1]) == 0:
                    continue
                else:
                    self.Update(result)
                    do_nothing = False

                #if self.IsAllSolved():
                #   break

            if do_nothing:
                ## All specs are scanned, may be multiple passes, and nothing can be done now
                break

        if self.IsAllSolved():
            ## Success
            # print("Sucessful here")
            # if not self.intermediate:
            #   self.Show()
            return [self]

        else:
            ## here's challenge, we have some multi-valued UV plus unsolved UV
            list_of_solutions = []
            list_of_comb = self.EnumerateMultiValue()
            #print(list_of_comb)

            for cb in list_of_comb:
                solver = self.Duplicate()
                solver.multi_valued = {}
                for r in cb:
                    solver.Update(r)

                #solver.Show()
                solver.intermediate = True
                sublist = solver.SolveIrp(irp)
                if solver.IsAllSolved():
                    #print("Sucessful after enum")
                    list_of_solutions.extend(sublist)
                    #solver.Show()
                    #self.Copy(solver)
                    #return

            try:
                list_of_solutions.remove(None)
            except:
                pass

            if len(list_of_solutions) == 0:
                # No any solution found, add my self, even though it's not a solved result
                list_of_solutions.append(self)

            return list_of_solutions

    def ShowMachineReadable(self, list_of_solutions):
        for r in list_of_solutions:
            r.Show()

    def HumanReadable(self, list_of_solutions):
        # list_of_solutions is the output from SolveIrp()
        # This function returns a human readable modsig string, where
        #   digit means solved UV value
        #   - means unsolved UV value
        #   * means UV value solved but with multiple solution

        all_solutions = []  # list of dict ("Mode": set([0]), ...)
        for s in list_of_solutions:
            d = {}
            for uv in s.solved:
                d[uv] = set([s.solved[uv]])
            for uv in s.unsolved:
                d[uv] = set()
            for uv in s.multi_valued:
                d[uv] = set(s.multi_valued[uv])

            # add d into the list
            all_solutions.append(d)

        # Now for each of UV, get the intersection as the final solution
        final = {}
        for solution in all_solutions:
            for uv in solution:
                if not uv in final:
                    final[uv] = solution[uv]
                else:
                    final[uv] &= solution[uv]

        # Now make a string to represent the final solution
        modsig = ""
        for uv in AllUV:
            size = len(final[uv])
            if size == 0:
                modsig += '-'
                if uv == "Temperature":
                    modsig += '-'
            elif size > 1:
                modsig += "*"
                if uv == "Temperature":
                    modsig += '*'
            else:
                v = list(final[uv])
                modsig += "%d"%v[0]

            if uv == "SwingV":
                modsig += '0'   # Add a SwingH after temperature

        return modsig

# Raw IR packet, only for pairing
class RawIrp(object):
    def __init__(self):
        self.lengths = []
        self.b = []

    def normalize(self):
        self.lengths, remap = normalize(self.lengths)
        for i, b in enumerate(self.b):
            self.b[i] = remap[b]

    def Load(self, f):
        # Load raw irp from a json object
        j = json.load(f)
        self.lengths = j["Lengths"]
        self.b = j["Main"]
        #self.normalize()

    def LoadbyStrings(self, length_str, bits_str):
        l = length_str.split(" ")
        for i in l:
            self.lengths.append(int(i, 10))

        for i in bits_str:
            self.b.append(int(i, 10))

        #self.normalize()

    def GetLengthVector(self):
        return {
            "Pulse": len(self.b),
            "Lengths": self.lengths
        }

    def GetBitsValue(self, bits):
        fb = list(bits)
        fb.sort()

        fv = []
        for i in fb:
            fv.append(self.b[i])

        return fv


def encode_irbcluster(obj):
    if not isinstance(obj, IrbCluster):
        raise TypeError(repr(obj) + " is not JSON serializable")

    irbs = []
    for i in obj.irbs:
        cb = i.GetConstantBits()
        cv = i.GetFeaturedValue(cb)

        irbs.append({
            "Device": i.device_id,
            "ConstantBits": list(cb),
            "ConstantValue": cv
        })

    return {
            "Number of irbs": len(obj.irbs),
            "Lengths": obj.length_vector["Lengths"],
            "Pulse": obj.length_vector["Pulse"],
            "FeaturedBits": list(obj.featured_bits) if obj.featured_bits else [],
            "FeaturedValue": obj.featured_value if obj.featured_value else [],
            "Irbs": irbs
            }

def compare_lengths(a, b):
    ''' This function compares two sorted lengths tables, and returns:
        - a boolean value to indicate if they are matched.
        - remap_dict for a
        - remap_dict for b

        For exmaple:
        a = [413, 625, 1509, 4055, 8452]
        b = [453, 1543, 4078, 4155, 8283]

        Notices that
        - 413 and 625 in a are matched with 453 in b.
        - 4078 and 4155 in b are matched with 4055 in a.

        So it returns (True, [0, 0, 1, 2, 3], [0, 1, 2, 2, 3]).
    '''

    def _f(x, centor):
        #print "test %d on center %d" %(x, centor)
        width = 200.0 + math.log(centor/4000.0, 10) * 1000.0
        if width < 600.0:
            width = 600.0
        tail = 25.0
        #print "width %f, tail %f" %(width, tail)

        if abs(x-centor) <= width:
            return 1.0
        else:
            return math.exp(-pow(abs(x-centor)-width, 2)/(2.0*pow(tail, 2)))

    remap_a = []
    for x in b:
        m = []
        sum = 0.0
        for y in a:
            p = _f(y, x)
            m.append(p)
            sum += p
        if sum < 0.5:
            return (False, None, None)

        c = []
        for i, v in enumerate(m):
            if v >= 0.5:
                c.append(i)
        if not remap_a:
            remap_a.append(c)
        elif remap_a[-1] != c:
            remap_a.append(c)

    remap_b = []
    for x in a:
        m = []
        sum = 0.0
        for y in b:
            p = _f(y, x)
            #print "%f" %p
            m.append(p)
            sum += p

        if sum < 0.5:
            return (False, None, None)

        c = []
        for i, v in enumerate(m):
            if v >= 0.5:
                c.append(i)
        if not remap_b:
            remap_b.append(c)
        if remap_b[-1] != c:
            remap_b.append(c)

    remap_dict_a = []
    for i, r in enumerate(remap_a):
        # r is a list
        for k in r:
            remap_dict_a.append(i)

    remap_dict_b = []
    for i, r in enumerate(remap_b):
        # r is a list
        for k in r:
            remap_dict_b.append(i)


    return (True, remap_dict_a, remap_dict_b)


class IrbCluster(object):
    def __init__(self, irb=None, featured_bits=None):
        self.device_id = ""         # device_id is from json by Load()
        self.irbs = []
        self.length_vector = {}     # length vector is a dict, see IrBase.GetLengthVector()
        self.featured_bits = None       # list of ordered bit index
        self.featured_value = None  # list of bit values corresponding to featured bits

        if irb:
            self.device_id = irb.device_id
            self.irbs.append(irb)
            self.length_vector = irb.GetLengthVector()

        if featured_bits:
            self.featured_bits = featured_bits
            self.featured_value = irb.GetFeaturedValue(featured_bits)

    def Show(self):
        print("Cluster has %d irb%s"%(self.Size(), "s" if self.Size() > 1 else ""))
        print("Length vector: ", self.length_vector)
        #print("Featured bits: ", self.featured_bits)
        #print("Featured value: ", self.featured_value)

        for i in self.irbs:
            print("  %s" %i.device_id)
            print("  %s" %i.device_id, i.GetLengthVector())
        #    print("  Value: ", i.GetFeaturedValue(self.featured_bits))
        #    cb = i.GetConstantBits()
        #    print("  Constant Bits: ", cb)
        #    print("  Constant: ", i.GetFeaturedValue(cb))
        #    # Construct a dict
        #    d = {}
        #    l = list(cb)
        #    l.sort()
        #    fv = i.GetFeaturedValue(cb)
        #    i = 0
        #    for index in l:
        #       d[index] = fv[i]
        #       i += 1
        #    print(d)

    def Dump(self, fp):
        # Dump json representation to a file
        json.dump(self, fp, indent=4, separators=(',', ': '), default=encode_irbcluster)

    def Load(self, j):
        self.device_id = j["Device"]
        self.length_vector["Pulse"] = j["Pulse"]
        self.length_vector["Lengths"] = j["Lengths"]
        self.featured_bits = set(j["FeaturedBits"])
        self.featured_value = j["FeaturedValue"]

    def CompareLengthVectors(self, lv0, lv1):
        # Return True if the two length vectors are the same

        if lv0["Pulse"] != lv1["Pulse"]:
            return False

        if len(lv0["Lengths"]) != len(lv1["Lengths"]):
            return False

        # compute Euclidean distance
        l0 = lv0["Lengths"]
        l1 = lv1["Lengths"]
        d = 0.0

        diff = False
        for k in range(len(l0)):
            if l0[k] >= 10000 and l1[k] >= 10000:
                if (l0[k] - l1[k]) * (l0[k] - l1[k]) > (500*500):
                    diff = True
                    break
            elif (l0[k] - l1[k]) * (l0[k] - l1[k]) > (200*200):
                diff = True
                break
        return not diff

    def CompareLengthVectors2(self, lv0, lv1):
        if lv0["Pulse"] != lv1["Pulse"]:
            return False

        l0 = lv0["Lengths"]
        l1 = lv1["Lengths"]
        ret, remap_a, remap_b = compare_lengths(l0, l1)
        return ret

    def IsLengthVectorMatched(self, irb):
        # Test if an irb matches this cluster
        lv = irb.GetLengthVector()
        assert len(lv) != 0
        return self.CompareLengthVectors(self.length_vector, lv)

    def IsFeaturedValueMatched(self, irb):
        # Test if an irb matches this cluster
        return self.featured_value == irb.GetFeaturedValue(self.featured_bits)

    def AddIrb(self, irb):
        self.irbs.append(irb)

    def Size(self):
        return len(self.irbs)

class PseudoCluster(object):

    ''' Pseudo cluster class for loading cluster definition from json object
    '''

    def __init__(self):
        self.irbs = []  ## list of pseudo irbs
        self.length_vector = {}
        self.featured_bits = None
        self.featured_value = None

    def load(self, j):
        ''' Load value from a json object '''

        self.length_vector["Pulse"] = j["Pulse"]
        self.length_vector["Lengths"] = j["Lengths"]
        self.featured_bits = set(j["FeaturedBits"])
        self.featured_value = j["FeaturedValue"]

        for irb in j["Irbs"]:
            self.irbs.append(irb)

    def compare_length_vector(self, lv):
        if self.length_vector["Pulse"] != lv["Pulse"]:
            return (False, None, None)

        ret, remap_a, remap_b = compare_lengths(self.length_vector["Lengths"], lv["Lengths"])
        print("    %s" %("*" if ret else "-"), self.length_vector["Lengths"])
        return (ret, remap_a, remap_b)

    def matched(self, raw_irp):
        # Test if a raw irp matches this cluster

        # Length vector comparison
        #c = IrbCluster()    ## Use true cluster's method
        #if not c.CompareLengthVectors(self.length_vector, raw_irp.GetLengthVector()):
        #    return False
        ret, remap_a, remap_b = self.compare_length_vector(raw_irp.GetLengthVector())
        if not ret:
            return False

        # In case of no featured bits
        if not self.featured_bits:
            return True

        # Featured code comparison
        # Before doing feature code comparison, both may need to remap
        #print(remap_a, remap_b)
        fv0 = [remap_a[v] for v in self.featured_value]
        fv1 = [remap_b[v] for v in raw_irp.GetBitsValue(self.featured_bits)]

        #fv0 = self.featured_value
        #fv1 = raw_irp.GetBitsValue(self.featured_bits)
        print("          ==> %f" %(matching_score(fv0, fv1)))
        return fv0 == fv1


def lv_classifier(cluster):
    # Length vector based classifier
    # return list of clusters

    if cluster.Size() <= 1:
        return [cluster]

    l = []
    for irb in cluster.irbs:
        if len(l) == 0:
            l.append(IrbCluster(irb, cluster.featured_bits))
        else:
            # We already have some clusters, each of them must be tested
            found = False
            for c in l:
                if c.IsLengthVectorMatched(irb):
                    c.AddIrb(irb)
                    found = True
                    break

            # No matched cluster found, add a new one
            if not found:
                l.append(IrbCluster(irb, cluster.featured_bits))

    return l


def fb_classifier(cluster):
    # Featured bits based classifier
    # return list of clusters

    if cluster.Size() <= 1:
        return [cluster]

    fb = None
    for irb in cluster.irbs:
        if not fb:
            fb = irb.GetConstantBits()
        else:
            fb &= irb.GetConstantBits()

    # Now s is the intersection of all featured bits

    l = []
    for irb in cluster.irbs:
        if len(l) == 0:
            l.append(IrbCluster(irb, fb))
        else:
            # We already have some clusters, each of them must be tested
            found = False
            for c in l:
                if c.IsFeaturedValueMatched(irb):
                    c.AddIrb(irb)
                    found = True
                    break

            # No matched cluster found, add a new one
            if not found:
                l.append(IrbCluster(irb, fb))

    return l

def chained_classify(cluster, list_of_classifiers):

    if cluster.Size() <= 1:
        return [cluster]

    list_of_clusters = [cluster]

    while True:
        before = len(list_of_clusters)
        for classifier in list_of_classifiers:
            result = []
            for c in list_of_clusters:
                if len(c.irbs) > 1:
                    l = classifier(c)
                    result.extend(l)
                else:
                    result.append(c)

            list_of_clusters = result

        after = len(list_of_clusters)
        if before == after:
            break

    return list_of_clusters

def twins_test(irb1, irb2):

    # Make a cluster of two irbs
    c = IrbCluster()
    c.AddIrb(irb1)
    c.AddIrb(irb2)

    result = chained_classify(c, [lv_classifier, fb_classifier])
    if len(result) == 1:
        return True
    else:
        return False

def fb2_classifier(cluster):

    nb_irbs = cluster.Size()
    #print("size of cluster: ", nb_irbs)
    if nb_irbs <= 2:
        return [cluster]

    lt = []
    for i in range(nb_irbs):
        lt.append(set([i]))

    for i in range(nb_irbs - 1):
        for j in range(i + 1, nb_irbs):
            irb1 = cluster.irbs[i]
            irb2 = cluster.irbs[j]
            if twins_test(irb1, irb2):
                lt.append(set([i, j]))

    while True:
        merged = False
        for i in range(len(lt)):
            if len(lt[i]) == 0:
                continue
            else:
                merged_inner = False
                for j in range(len(lt)):
                    if i == j:
                        continue

                    if lt[i] & lt[j]:
                        lt[j] |= lt[i]
                        merged_inner = True

                if merged_inner:
                    lt[i] = set([])
                    merged = True

        if not merged:
            break

    #print(lt)
    lc = []
    for s in lt:
        if not s:
            continue

        ## Make cluster out of the set
        fb = None
        for i in s:
            irb = cluster.irbs[i]
            if not fb:
                fb = irb.GetConstantBits()
            else:
                fb &= irb.GetConstantBits()

        c = None
        for i in s:
            irb = cluster.irbs[i]
            if not c:
                c = IrbCluster(irb, fb)
            else:
                c.AddIrb(irb)

        lc.append(c)

    return lc

def Classify(list_of_irbs):

    # Create a clsuter to include all the irbs
    c = IrbCluster()
    for i in list_of_irbs:
        c.AddIrb(i)

    return chained_classify(c, [lv_classifier, fb_classifier, fb2_classifier])
    #return chained_classify(c, [lv_classifier])

def AnalyzeFile(filename):
    irb = IrBase()
    try:
        irb.ReadFile(filename)
    except IOError as e:
        print("Read file failed")
        return
    except ValueError as e:
        print("Parse file failed")
        return

    # Clear the final specs
    bpa = BitPositionAnalyzer(irb)
    if options.trace_bpa:
        bpa.Show()
    bsa = BitStabilityAnalyzer(irb, bpa)
    bsa.Show()

    return bsa.final_specs


def matching_score(l0, l1):
    assert len(l0) == len(l1), "Comparing two lists with different size"

    size = len(l0)
    c = 0
    for i in range(size):
        if l0[i] == l1[i]:
            c += 1

    return float(c) / size;

def Pair(clusters, raw_irp):

    """ Do pairing,
        Input:
            - clusters, list of clusters
            - raw_irp, raw IRP object
        Output: return a cluster object
    """
    match = None
    for c in clusters:
        if c.matched(raw_irp):
            match = c
            break

    if not match:
        return [65535]

    ids = []
    print("Found a matching cluster ...")
    s = ""
    for b in match.featured_value:
        s += str(b)
    print("[[%s]]" %s)
    print("Looking into the irbs ...")

    ret, remap_a, remap_b = compare_lengths(match.length_vector["Lengths"], raw_irp.lengths)

    for irb in match.irbs:
        bv1 = [remap_a[v] for v in irb["ConstantValue"]]
        bv2 = [remap_b[v] for v in raw_irp.GetBitsValue(irb["ConstantBits"])]
        for bv in [bv1, bv2]:
            s = ""
            for b in bv:
                s += str(b)
            print("<<%s>>" %s)

        print("IRB %s score: %f" %(irb["Device"], matching_score(bv1, bv2)))

        #if raw_irp.GetBitsValue(irb["ConstantBits"]) == irb["ConstantValue"]:
        if bv1 == bv2:
            ids.append(int(irb["Device"].split("-")[0], 10))

    return ids

def Pair2(all_x_irbs, r):

    ''' Do pairing for a given RawIrp.
        - all_x_irbs: list of irbs which are loaded from irb json file.
                      Note: this irb is not Irbase instance.
        - r: RawIrp object.
    '''

    scores = []
    for irb in all_x_irbs:
        # compare r against irb
        lengths = [v for v in r.lengths]
        bits = [b for b in r.b]

        if len(bits) != irb["pulses"]:
            continue

        print("\n--> Compare with", irb["device_id"])

        # Check lengths against centors
        limits = []
        centors = irb["centors"]
        for i in range(len(centors)):
            if i == 0:
                limits.append((0, (centors[1] - centors[0])/2.0 + centors[0] ))
            elif i == (len(centors) - 1):
                limits.append((centors[i] - (centors[i] - centors[i-1])/2.0, 100000.0))
            else:
                limits.append((centors[i] - (centors[i] - centors[i-1])/2.0, centors[i] + (centors[i+1]-centors[i])/2.0))

        remap = []
        for l in lengths:
            for i, lmt in enumerate(limits):
                if (lmt[0] <= l) and (l < lmt[1]):
                    remap.append(i)
                    break

        print("limits", limits)
        print("remap", remap)
        print("centors", centors)
        print("irb/lengths", irb["lengths"])

        ## Check remap
        failed = False
        for i in range(len(remap)):
            if i == 0:
                if remap[0] != 0:
                    failed = True
                    break
            elif remap[i] == remap[i-1]:
                continue
            elif remap[i] != remap[i-1] + 1:
                failed = True
                break

        last = remap[-1]

        if failed:
            print("remap checking failed 1")
            continue

        if last != len(centors) -1:
            print("remap checking failed 2", last)
            continue

        # calculate standard deviation vector so that we can evaluate how the input irp matches an irb
        variance = []
        gaussian = []
        for i, c in enumerate(centors):
            lt = []
            for j, rm in enumerate(remap):
                if rm == i:
                    lt.append(lengths[j])
            var = 0.0
            for l in lt:
                var += (l-c) ** 2

            variance.append(var/len(lt))
            gaussian.append(math.exp(-pow((c - float(sum(lt))/len(lt)), 2.0) / (2 * 10000.0)))

        print("*", irb["device_id"], irb["lengths"])

        new_bits = [remap[b] for b in bits]
        fv = [new_bits[i] for i in irb["fb"]]

        score = 0
        for i, b in enumerate(irb["fv"]):
            if fv[i] == b:
                score += 1
        #scores.append((float(score)/len(fv), irb["device_id"]))
        #scores.append((float(score)/len(fv), score, len(fv), irb["device_id"], variance, gaussian))

        p = 1.0
        for g in gaussian:
            p *= g
        scores.append((float(score)/len(fv), p, score, len(fv), irb["device_id"], variance, gaussian))

    scores.sort(cmp, reverse=True)
    print("\n>>>")
    for i, score in enumerate(scores):
        if i > 10:
            break
        else:
            print("  > ID", score[4], ", score %f, %d/%d" %(score[0], score[2], score[3]), score[1], "variance", score[5], score[6])


    candidates = [score for score in scores if score[0] == 1.0]

    if len(candidates) > 1:
        ## remove the items if they have significantly lower rank than other
        to_be_removed = []
        for i, c in enumerate(candidates):
            for ci in candidates:
                if (c[1] < ci[1]) and (((ci[1] - c[1]) / c[1]) >= 0.05):
                    to_be_removed.append(i)
        return [c[4] for i, c in enumerate(candidates) if i not in to_be_removed]
    elif len(candidates) == 1:
        return [candidates[0][4]]
    elif scores:
        return [scores[0][4]]
    else:
        return []


def ClassifyIrbs(input_dir, dump_cluster_file=""):
    if not os.path.isdir(input_dir):
        raise RuntimeError("Trying to do classification on a non-directory")

    all_irbs = []
    for f in [os.path.join(input_dir, fn) for fn in next(os.walk(input_dir))[2]]:
        ib = IrBase()
        ib.ReadFile(f)
        for n in ib.pulses:
            all_irbs.append(ib.sub_irb(n))

    all_clusters = Classify(all_irbs)
    if dump_cluster_file != "":
        # Dump json objects to the file
        f = open(dump_cluster_file, "wb")
        f.write("[\n")
        for i, c in enumerate(all_clusters):
            c.Dump(f)
            if i != len(all_clusters) - 1:
                f.write(",\n")
        f.write("]\n")
        f.close()

    return all_clusters

def LoadClustersFile(cluster_file):

    """ Return a list of all pseudo clusters
    """
    f = open(cluster_file, "rb")
    cl = json.load(f)
    f.close()

    all_clusters = []
    for j in cl:
        c = PseudoCluster()
        c.load(j)
        all_clusters.append(c)

    return all_clusters

def LoadJsonSpecFile(spec_file):

    f = open(spec_file, "rb")
    uvl = json.load(f)
    f.close()

    all_specs = []
    for obj in uvl:
        uvspec = UVSpec()
        uvspec.name = obj["Name"]
        uvspec.conditions = obj["Conditions"]
        uvspec.list_of_index = obj["Index"]
        uvspec.uv_values = obj["Keys"]
        uvspec.uv_values.sort()

        # An integer key of a Python dictionary was changed to string
        # as json doesn't allow integer keys
        # So for uv_bits_in_list,  we need to change the string key back to integer key
        for v in obj["Values"]:
            uvspec.uv_bits_in_list[int(v)] = obj["Values"][v]
        #uvspec.Show()
        all_specs.append(uvspec)

    return all_specs

def DumpJsonSpecFile(output_spec_file, all_specs):
    f = open(output_spec_file, "wb")
    f.write("[\n")
    for i, uvspec in enumerate(all_specs):
        if i > 0:
            f.write(",")
        uvspec.Dump(f)
    f.write("]\n")
    f.close()

def DumpDeviceSummaryJson(spec_file):
    """ Note: this API only works when Mode, Temperature, Fan have unconditional spec
    """
    all_specs = LoadJsonSpecFile(spec_file)

    mode_spec = None
    temperature_spec = None
    fan_spec = None

    for s in all_specs:
        if s.name == "Mode" and not s.conditions:
            mode_spec = s
        elif s.name == "Temperature" and not s.conditions:
            temperature_spec = s
        elif s.name == "Fan" and not s.conditions:
            fan_spec = s

    dict_t = {}
    dict_f = {}

    try:
        for m in mode_spec.uv_values:
            dict_t[m] = temperature_spec.uv_values
            dict_f[m] = fan_spec.uv_values

        d = {}
        d["Mode"] = mode_spec.uv_values
        d["Temperature"] = dict_t
        d["Fan"] = dict_f
    except AttributeError as e:
        print("Failed to generate range description")
        d = {}

    return json.dumps(d)

def innolinks_pair(cluster_file, lengths_str, bits_str):
    """ Execute a pairing search.

            cluster_file: a string of cluster file name by which pairing search can be done.
            lengths_str: a string that contains length table. Lengths are decimal, separated by while space. For example:
                "597 1694 4503 8927".
            bits_str: a string that contains all the bits information. For example:
                "32010100000001000100000000000100000000000001000".

            Return the device ID, as an integer, if successful, or 0xffff if failed.
    """
    device_id = 0xffff
    log.info("Pair:")
    log.info("Length %s", lengths_str)
    log.info("main %s", bits_str)

    try:
        f = open(cluster_file, "rb")
        all_x_irbs = json.load(f)
        f.close()

        r = RawIrp()
        r.LoadbyStrings(lengths_str, bits_str)
        ids = Pair2(all_x_irbs, r)
        device_id = int(ids[0].split("-")[0], 10)
        log.info("return %d", device_id)
    except:
        log.exception("Pair failed")

    log.info("")
    return device_id

def innolinks_demodsig(spec_path, device_id, bits_str):
    """ Decode the IR bit stream into a modsig.

            spec_path: a string of directory that contains IR specification files by which modsig decoding can be done.
            device_id: an integer to indicator IR device ID.
            bits_str: a string that contains all the bits information. For example:
                "32010100000001000100000000000100000000000001000".

            Return modsig string. For example: "0160031".
    """
    modsig = ""
    log.info("Demodsig:")
    log.info("Device Id %d", device_id)
    log.info("main %s", bits_str)

    try:
        # Construct UV specs from the json file
        filename = os.path.join(spec_path, "HD_%04d.json" %device_id)
        all_specs = LoadJsonSpecFile(filename)
        irp = IrPacket()
        irp.OnMain(bits_str)
        solver = IrpSolver(all_specs)
        l = solver.SolveIrp(irp)
        modsig = solver.HumanReadable(l)
        log.info("return %s", modsig)
    except:
        log.exception("Demodsig failed")

    log.info("")
    return modsig

def innolinks_get_summary(spec_path, device_id):
    """ Get the summary of a spec. The summary is a json string like this:

            {
              "Mode": [1, 2, 4],
              "Fan":
                {"1": [1, 2, 3, 6],
                 "2": [1, 2, 3, 6],
                 "4": [1, 2, 3, 6]
                },
              "Temperature":
                {"1": [32, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31],
                 "2": [32, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31],
                 "4": [32, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31]
                }
            }

            spec_path: a string of directory that contains IR specification files by which modsig decoding can be done.
            device_id: an integer to indicator IR device ID.

            Return the summary json string.
    """
    filename = os.path.join(spec_path, "HD_%04d.json" %device_id)
    return DumpDeviceSummaryJson(filename)
