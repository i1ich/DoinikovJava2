package ru.spbstu.pipeline;

public interface IConfigurable {
    RC setConfig(String cfg);
    RC setConsumer(IExecutable c);
    RC setProducer(IExecutable p);
}
