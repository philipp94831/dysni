import plotly.plotly as py
import plotly.graph_objs as go

xValues = []
yValues = []
maxY = 0

filename = 'CD_incremental'

with open(filename + '.txt', 'r') as inFile:
	for line in inFile:
		xValues.append(int(line.split(',')[0]))
		y = int(line.split(',')[1])
		if y > maxY:
			maxY = y
		yValues.append(y)

print("maxY: " + str(maxY))

# Create a trace
trace = go.Scatter(
    x = xValues,
    y = yValues
)

data = [trace]

# Plot and embed in ipython notebook!
# py.iplot(data, filename=filename + '.plot')

plot_url = py.plot(data, filename='basic-line')
print(plot_url)