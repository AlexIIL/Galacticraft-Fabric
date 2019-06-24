import os
import json
import sys


def mod_id(texture, cfg):
    return cfg['mod-id'] + ":" + texture


writes = 0
models = []


class Model:
    def __init__(self, north, south, east, west, up, down, particle, cfg, tag):
        self.north = mod_id(north, cfg)
        self.south = mod_id(south, cfg)
        self.east = mod_id(east, cfg)
        self.west = mod_id(west, cfg)
        self.up = mod_id(up, cfg)
        self.down = mod_id(down, cfg)
        self.particle = mod_id(particle, cfg)
        self.tag = tag
        self.cfg = cfg

    def save(self):
        filename = os.path.join("model_output", "models", "block", self.cfg['name'], self.tag + ".json")
        if os.path.exists(filename):
            os.remove(filename)

        model_output = {
            "parent": "block/cube",
            "textures": {
                "north": self.north,
                "south": self.south,
                "east": self.east,
                "west": self.west,
                "up": self.up,
                "down": self.down,
                "particle": self.particle
            }
        }

        if not os.path.exists(os.path.dirname(filename)):
            os.makedirs(os.path.dirname(filename))
        file = open(filename, 'w+')
        file.write(json.dumps(model_output, indent=4))
        file.close()
        models.append(self.tag)


# Print iterations progress
def print_progress(iteration, total, prefix='', suffix='', decimals=1, bar_length=100):
    """
    Call in a loop to create terminal progress bar
    @params:
        iteration   - Required  : current iteration (Int)
        total       - Required  : total iterations (Int)
        prefix      - Optional  : prefix string (Str)
        suffix      - Optional  : suffix string (Str)
        decimals    - Optional  : positive number of decimals in percent complete (Int)
        bar_length  - Optional  : character length of bar (Int)
    """
    str_format = "{0:." + str(decimals) + "f}"
    percents = str_format.format(100 * (iteration / float(total)))
    filled_length = int(round(bar_length * iteration / float(total)))
    bar = '█' * filled_length + '-' * (bar_length - filled_length)

    sys.stdout.write('\r%s |%s| %s%s %s' % (prefix, bar, percents, '%', suffix)),

    if iteration == total:
        sys.stdout.write('\n')
    sys.stdout.flush()


config_file = open('model_config.json', 'r')
config = json.loads(config_file.read())
sides = config['sides']
faces = ["north", "south", "east", "west"]
options = config['options']
options.append({'option': 'default'})
print(options)
state = json.loads('{"variants": {}}')

if os.path.exists("model_output"):
    os.remove("model_output")
    os.mkdir("model_output")

progress = 0
print_progress(0, 50, prefix="Progress:", suffix="Complete", bar_length=50)


for north in options:
    if north['option'] == 'default':
        n_texture = sides['north']['texture']
    else:
        n_texture = north['texture']
    for south in options:
        if south['option'] == 'default':
            s_texture = sides['south']['texture']
        else:
            s_texture = south['texture']
        for east in options:
            if east['option'] == 'default':
                e_texture = sides['east']['texture']
            else:
                e_texture = east['texture']
            for west in options:
                if west['option'] == 'default':
                    w_texture = sides['west']['texture']
                else:
                    w_texture = west['texture']
                for up in options:
                    if up['option'] == 'default':
                        u_texture = sides['up']['texture']
                    else:
                        u_texture = up['texture']
                    for down in options:
                        if down['option'] == 'default':
                            d_texture = sides['down']['texture']
                        else:
                            d_texture = down['texture']

                        tag = ''.join([
                            north['option'],
                            '-',
                            south['option'],
                            '-',
                            east['option'],
                            '-',
                            west['option'],
                            '-',
                            up['option'],
                            '-',
                            down['option']
                        ])

                        model = Model(n_texture, s_texture, e_texture, w_texture, u_texture, d_texture, config['particle'], config, tag)
                        model.save()
                        writes += 1
                        progress += 1
                        print_progress(((progress / len(options)**6) * 50), 50, prefix="Progress:", suffix="Complete", bar_length=50)

y = -90
for face in faces:
    y += 90
    for tag in models:
        state['variants'][''.join(["facing=", face, ",sides=", tag])] = {
            "model": config['mod-id'] + ':block/' + config['name'] + "/" + tag,
            "y": y
        }

os.makedirs("model_output/blockstates/")
os.makedirs("model_output/models/item/")
blockstates = open('model_output/blockstates/' + config['name'] + ".json", 'w+')
blockstates.write(json.dumps(state, indent=4))
writes += 1
blockstates.close()
item = open('model_output/models/item/' + config['name'] + '.json', 'w+')
item.write(json.dumps({"parent": config['mod-id'] + ":block/" + config['name'] + "/default-default-default-default-default-default"}, indent=4))
writes += 1
item.close()
config_file.close()

java = open('model_output/java-prop.txt', 'w+')

class_name = ""
for s in config["name"].split('_'):
    class_name += s.capitalize()

class_name += "Sides"

enum_text = "public enum " + class_name + " {\n"

for model in models:
    enum_text += "    " + model.upper() + "(\"" + model.lower() + "\"),\n"

enum_text += "    ;\n\n    String name;\n    " + class_name + "(String name) implements StringIdentifiable {\n        this.name = name;\n    }\n    @Override\n    public String asString() {\n        return this.name;\n    }\n}"

java_text = ''.join([
    "private final static DirectionProperty FACING = DirectionProperty.of(\"facing\", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);\n",
    "private final static EnumProperty SIDES = EnumProperty.of(\"sides\", " + class_name + ".class);\n",
    "\n",
    "@Override\n",
    "public void appendProperties(StateFactory.Builder<Block, BlockState> stateBuilder) {\n",
    "    super.appendProperties(stateBuilder);\n",
    "    stateBuilder.add(FACING);\n",
    "    stateBuilder.add(SIDES);\n",
    "}\n",
    "\n",
    "@Override\n",
    "public BlockState getPlacementState(ItemPlacementContext context) {\n",
    "    return this.getDefaultState().with(FACING, context.getPlayerFacing().getOpposite()).with(SIDES, " + class_name + ".DEFAULT_DEFAULT_DEFAULT_DEFAULT_DEFAULT_DEFAULT);\n",
    "}\n\n\n\n",
    enum_text
])
java.write(java_text)
java.close()


print("Finished creating files.")
print(''.join([str(writes), ' total files written.']))
print('Remember to delete the "model_output" folder before running again.')